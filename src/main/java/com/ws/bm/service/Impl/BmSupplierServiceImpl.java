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
import com.ws.bm.domain.entity.BmSupplier;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmSupplierMapper;
import com.ws.bm.service.IBmSupplierService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmSupplierServiceImpl extends ServiceImpl<BmSupplierMapper, BmSupplier> implements IBmSupplierService {
    @Override
    public boolean addBmSupplier(BmSupplier bmSupplier) {
        //校验必填字段
        if (checkFiled(bmSupplier)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        //名称重复校验
        QueryWrapper<BmSupplier> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmSupplier::getSupplierName,bmSupplier.getSupplierName());
        wrapper.lambda().eq(BmSupplier::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.supplier.nameRepeat"));
        }
        if (!InitFieldUtil.initField(bmSupplier)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmSupplier.setSupplierId(UUID.randomUUID().toString());
        bmSupplier.setWeight(0);
        return save(bmSupplier);
    }

    @Override
    public List<BmSupplier> queryBmSupplier(BmSupplier bmSupplier) {
        if (ObjectUtil.isEmpty(bmSupplier)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmSupplier> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmSupplier.getSupplierName())){
            wrapper.lambda().like(BmSupplier::getSupplierName,bmSupplier.getSupplierName());
        }
        if (StrUtil.isNotEmpty(bmSupplier.getPhone())){
            wrapper.lambda().like(BmSupplier::getPhone,bmSupplier.getPhone());
        }
        if (StrUtil.isNotEmpty(bmSupplier.getAddress())){
            wrapper.lambda().like(BmSupplier::getAddress,bmSupplier.getAddress());
        }
        wrapper.lambda().eq(BmSupplier::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().orderByDesc(BmSupplier::getWeight).orderByDesc(BmSupplier::getUpdateDate);
        return list(wrapper);
    }

    @Override
    public boolean deleteBmSupplier(List<String> bmSupplierIds) {

        if (CollUtil.isEmpty(bmSupplierIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmSupplier> bmSuppliers = listByIds(bmSupplierIds);
        if (CollUtil.isEmpty(bmSuppliers)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        bmSuppliers.forEach(bmSupplier -> {
            bmSupplier.setDeleted(BaseConstant.TRUE);
            bmSupplier.setUpdateDate(new Date());
        });

        return updateBatchById(bmSuppliers);
    }

    @Override
    public boolean updateBmSupplier(BmSupplier bmSupplier) {
        if (ObjectUtil.isEmpty(bmSupplier)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        //判断供应商名称是否重复
        QueryWrapper<BmSupplier> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmSupplier::getSupplierName,bmSupplier.getSupplierName().trim());
        wrapper.lambda().eq(BmSupplier::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmSupplier::getSupplierId,bmSupplier.getSupplierId());
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.supplier.nameRepeat"));
        }

        //旧对象
        BmSupplier oldSupplier = getById(bmSupplier.getSupplierId());
        if (!updateFlag(bmSupplier,oldSupplier)){
            oldSupplier.setSupplierName(bmSupplier.getSupplierName());
            oldSupplier.setAddress(bmSupplier.getAddress());
            oldSupplier.setPhone(bmSupplier.getPhone());
            oldSupplier.setRemark(bmSupplier.getRemark());
        }
        return updateById(oldSupplier);
    }

    @Override
    public BmSupplier getBmSupplier(String bmSupplierId) {
        if (StrUtil.isEmpty(bmSupplierId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmSupplier bmSupplier = getById(bmSupplierId);
        if (ObjectUtil.isEmpty(bmSupplier) || StrUtil.equals(bmSupplier.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.supplier.notexist"));
        }
        return bmSupplier;
    }

    @Override
    public List<BmSupplier> getBmSuppliers() {
        QueryWrapper<BmSupplier> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmSupplier::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().orderByDesc(BmSupplier::getWeight).orderByDesc(BmSupplier::getUpdateDate);
        return list(wrapper);
    }

    private boolean checkFiled(BmSupplier bmSupplier){
        if(ObjectUtil.isEmpty(bmSupplier) || StrUtil.isEmpty(bmSupplier.getSupplierName())){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmSupplier newObj, BmSupplier oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getSupplierName());
        sb2.append(oldObj.getSupplierName());
        sb1.append(newObj.getAddress());
        sb2.append(oldObj.getAddress());
        sb1.append(newObj.getPhone());
        sb2.append(oldObj.getPhone());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }
}
