package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmMaterial;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmMaterialMapper;
import com.ws.bm.service.IBmMaterialService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmMaterialServiceImpl extends ServiceImpl<BmMaterialMapper, BmMaterial> implements IBmMaterialService {
    @Override
    public boolean addBmMaterial(BmMaterial bmMaterial) {
        //必填校验
        if (checkFiled(bmMaterial)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //商品名称校验
        QueryWrapper<BmMaterial> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMaterial::getMaterialName,bmMaterial.getMaterialName());
        wrapper.lambda().eq(BmMaterial::getMaterialType,bmMaterial.getMaterialType());
        wrapper.lambda().eq(BmMaterial::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.material.nameRepeat"));
        }
        if (!InitFieldUtil.initField(bmMaterial)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmMaterial.setMaterialId(UUID.randomUUID().toString());
        bmMaterial.setBuyWeight(0);
        bmMaterial.setSellWeight(0);
        bmMaterial.setMakeWeight(0);
        return save(bmMaterial);
    }

    @Override
    public List<BmMaterial> queryBmMaterial(BmMaterial bmMaterial) {
        if (ObjectUtil.isEmpty(bmMaterial)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmMaterial> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmMaterial.getMaterialName())){
            wrapper.lambda().like(BmMaterial::getMaterialName,bmMaterial.getMaterialName());
        }
        if (StrUtil.isNotEmpty(bmMaterial.getMaterialType())){
            wrapper.lambda().eq(BmMaterial::getMaterialType,bmMaterial.getMaterialType());
        }
        wrapper.lambda().eq(BmMaterial::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().orderByDesc(BmMaterial::getUpdateDate);
        return list(wrapper);
    }

    @Override
    public boolean deleteBmMaterial(List<String> bmMaterialIds) {
        if (CollUtil.isEmpty(bmMaterialIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmMaterial> bmMaterials = listByIds(bmMaterialIds);
        if (CollUtil.isEmpty(bmMaterials)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmMaterials.forEach(bmMaterial -> {
            bmMaterial.setDeleted(BaseConstant.TRUE);
            bmMaterial.setUpdateDate(new Date());
        });

        return updateBatchById(bmMaterials);
    }

    @Override
    public boolean updateBmMaterial(BmMaterial bmMaterial) {
        if (checkFiled(bmMaterial)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        //判断名称是否重复
        QueryWrapper<BmMaterial> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMaterial::getMaterialName,bmMaterial.getMaterialName());
        wrapper.lambda().eq(BmMaterial::getMaterialType,bmMaterial.getMaterialType());
        wrapper.lambda().eq(BmMaterial::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmMaterial::getMaterialId,bmMaterial.getMaterialId());
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.material.nameRepeat"));
        }

        //旧对象
        BmMaterial oldMaterial = getById(bmMaterial.getMaterialId());
        if (!updateFlag(bmMaterial,oldMaterial)){
            oldMaterial.setMaterialName(bmMaterial.getMaterialName());
            oldMaterial.setMaterialType(bmMaterial.getMaterialType());
            oldMaterial.setRemark(bmMaterial.getRemark());
        }

        return updateById(oldMaterial);
    }

    @Override
    public BmMaterial getBmMaterial(String bmMaterialId) {
        if (StrUtil.isEmpty(bmMaterialId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmMaterial bmMaterial = getById(bmMaterialId);
        if (ObjectUtil.isEmpty(bmMaterial) || StrUtil.equals(bmMaterial.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.material.notexist"));
        }

        return bmMaterial;
    }

    @Override
    public List<BmMaterial> queryBmMaterialOrder(String materialType) {
        if (StrUtil.isEmpty(materialType)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmMaterial> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMaterial::getDeleted,BaseConstant.FALSE);
        if (StrUtil.equals(materialType,BaseConstant.MaterialOrderBySell)){
            wrapper.lambda().orderByDesc(BmMaterial::getSellWeight).orderByDesc(BmMaterial::getUpdateDate);
        }else if (StrUtil.equals(materialType, BaseConstant.MaterialOrderByBuy)){
            wrapper.lambda().orderByDesc(BmMaterial::getBuyWeight).orderByDesc(BmMaterial::getUpdateDate);
        }else if (StrUtil.equals(materialType,BaseConstant.MaterialOrderByMake)){
            wrapper.lambda().orderByDesc(BmMaterial::getMakeWeight).orderByDesc(BmMaterial::getUpdateDate);
        }else {
            wrapper.lambda().orderByDesc(BmMaterial::getUpdateDate);
        }

        return list(wrapper);
    }


    private boolean checkFiled(BmMaterial bmMaterial){
        if(ObjectUtil.isEmpty(bmMaterial) ||
                StrUtil.isEmpty(bmMaterial.getMaterialName()) || StrUtil.isEmpty(bmMaterial.getMaterialType())){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmMaterial newObj, BmMaterial oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getMaterialName());
        sb2.append(oldObj.getMaterialName());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }
}
