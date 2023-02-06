package com.ws.bm.service.system.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.system.BmDictData;
import com.ws.bm.domain.entity.system.BmDictType;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.system.BmDictMapper;
import com.ws.bm.service.system.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BmDictServiceImpl implements IBmDictService {

    @Autowired
    private BmDictMapper bmDictMapper;

    @Override
    public int addBmDictType(BmDictType bmDictType) {
        //参数校验
        if(ObjectUtil.isEmpty(bmDictType) || StrUtil.hasEmpty(bmDictType.getDictType(),bmDictType.getDictName()) || ObjectUtil.isEmpty(bmDictType.getOrderNum())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //校验字典类型名称是否重复
        if (CollUtil.isNotEmpty(bmDictMapper.queryDictByDictType(bmDictType.getDictType()))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dict.nameRepeat"));
        }
        //初始化基本属性
        if (!InitFieldUtil.initField(bmDictType)){
            throw new BaseException(HttpStatus.ERROR, MessageUtil.getMessage("bm.initFieldError"));
        }

        return bmDictMapper.addBmDictType(bmDictType);
    }

    @Override
    public List<BmDictType> queryBmDictType(BmDictType bmDictType) {
        return bmDictMapper.queryBmDictType(bmDictType);
    }

    @Override
    public int updateBmDictType(BmDictType bmDictType) {
        if(ObjectUtil.isEmpty(bmDictType) || StrUtil.hasEmpty(bmDictType.getDictType(),bmDictType.getDictName()) || ObjectUtil.isEmpty(bmDictType.getOrderNum())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //校验字典类型名称是否重复
        List<BmDictType> repeats = bmDictMapper.queryDictByDictType(bmDictType.getDictType());
        if (CollUtil.isNotEmpty(repeats) &&
                CollUtil.isNotEmpty(repeats.stream().filter(o -> !o.getId().equals(bmDictType.getId())).collect(Collectors.toList()))
        ){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dict.nameRepeat"));
        }
        BmDictType old = bmDictMapper.getBmDictType(bmDictType.getId());
        if(!dictTypeUpdateFlag(old,bmDictType)){
            old.setDictType(bmDictType.getDictType());
            old.setDictName(bmDictType.getDictName());
            old.setOrderNum(bmDictType.getOrderNum());
            old.setRemark(bmDictType.getRemark());
            old.setUpdateDate(new Date());
        }
        return bmDictMapper.updateBmDictType(old);
    }

    @Override
    public BmDictType getBmDictType(String bmDictId) {
        if (StrUtil.isEmpty(bmDictId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmDictMapper.getBmDictType(bmDictId);
    }

    @Override
    @Transactional
    public int deleteBmDictType(List<String> bmDictIds) {
        if (CollUtil.isEmpty(bmDictIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmDictIds.forEach(bmDictId -> {
            bmDictMapper.deleteBmDictDataByType(bmDictId);
        });
        return bmDictMapper.deleteBmDictType(bmDictIds);
    }

    @Override
    public int addBmDictData(BmDictData bmDictData) {
        if (ObjectUtil.isEmpty(bmDictData) || StrUtil.hasEmpty(bmDictData.getDictTypeId(),bmDictData.getDictCode(),bmDictData.getDictName()) ||
                ObjectUtil.isEmpty(bmDictData.getOrderNum())
        ){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        if (CollUtil.isNotEmpty(bmDictMapper.checkBmDictDataUnique(bmDictData))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.dict.nameRepeat"));
        }

        //初始化基本属性
        if (!InitFieldUtil.initField(bmDictData)){
            throw new BaseException(HttpStatus.ERROR, MessageUtil.getMessage("bm.initFieldError"));
        }

        return bmDictMapper.addBmDictData(bmDictData);
    }

    @Override
    public int updateBmDictData(BmDictData bmDictData) {

        if (ObjectUtil.isEmpty(bmDictData) || StrUtil.hasEmpty(bmDictData.getDictTypeId(),bmDictData.getDictCode(),bmDictData.getDictName()) ||
                ObjectUtil.isEmpty(bmDictData.getOrderNum())
        ){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (CollUtil.isNotEmpty(bmDictMapper.checkBmDictDataUnique(bmDictData))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.dict.nameRepeat"));
        }
        BmDictData old = bmDictMapper.getBmDictData(bmDictData.getId());

        if (!dictDataUpdateFlag(old,bmDictData)){
            old.setDictCode(bmDictData.getDictCode());
            old.setDictName(bmDictData.getDictName());
            old.setOrderNum(bmDictData.getOrderNum());
            old.setRemark(bmDictData.getRemark());
            old.setUpdateDate(new Date());
        }

        return bmDictMapper.updateBmDictData(old);
    }

    @Override
    public BmDictData getBmDictData(String bmDictId) {
        if (StrUtil.isEmpty(bmDictId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmDictMapper.getBmDictData(bmDictId);
    }

    @Override
    public List<BmDictData> queryBmDictData(BmDictData bmDictData) {
        return bmDictMapper.queryBmDictData(bmDictData);
    }

    @Override
    public int deleteBmDictData(List<String> bmDictIds) {

        if (CollUtil.isEmpty(bmDictIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmDictMapper.deleteBmDictData(bmDictIds);
    }

    @Override
    public List<BmDictData> getDictDataByType(String bmDictType) {

        if (StrUtil.isEmpty(bmDictType)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmDictMapper.getDictDataByType(bmDictType);
    }

    //根据字典类型和字典名称获得唯一的字典
    @Override
    public BmDictData getSoleDict(String bmDictType, String bmDictCode){
        if (StrUtil.hasEmpty(bmDictType, bmDictCode)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        return bmDictMapper.getSoleDict(bmDictType,bmDictCode);
    }

    private boolean dictTypeUpdateFlag(BmDictType oldObj,BmDictType newObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(oldObj.getDictType());
        sb2.append(newObj.getDictType());
        sb1.append(oldObj.getDictName());
        sb2.append(newObj.getDictName());
        sb1.append(oldObj.getOrderNum());
        sb2.append(newObj.getOrderNum());
        sb1.append(oldObj.getRemark());
        sb2.append(newObj.getRemark());

        return sb1.toString().equals(sb2.toString());
    }

    private boolean dictDataUpdateFlag(BmDictData oldObj,BmDictData newObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(oldObj.getDictCode());
        sb2.append(newObj.getDictCode());
        sb1.append(oldObj.getDictName());
        sb2.append(newObj.getDictName());
        sb1.append(oldObj.getOrderNum());
        sb2.append(newObj.getOrderNum());
        sb1.append(oldObj.getRemark());
        sb2.append(newObj.getRemark());

        return sb1.toString().equals(sb2.toString());
    }


}
