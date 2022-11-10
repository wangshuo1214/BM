package com.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bm.common.constant.HttpStatus;
import com.bm.common.utils.InitFieldUtil;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmDictData;
import com.bm.domain.entity.BmDictType;
import com.bm.exception.BaseException;
import com.bm.mapper.BmDictMapper;
import com.bm.service.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(ObjectUtil.isEmpty(bmDictType) || StrUtil.hasEmpty(bmDictType.getDictType(),bmDictType.getDictName(),bmDictType.getStatus()) || ObjectUtil.isEmpty(bmDictType.getOrderNum())){
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
        if(ObjectUtil.isEmpty(bmDictType) || StrUtil.hasEmpty(bmDictType.getDictType(),bmDictType.getDictName(),bmDictType.getStatus()) || ObjectUtil.isEmpty(bmDictType.getOrderNum())){
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
            old.setStatus(bmDictType.getStatus());
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
    public int deleteBmDictType(List<String> bmDictIds) {
        if (CollUtil.isEmpty(bmDictIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
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
            old.setStatus(bmDictData.getStatus());
            old.setOrderNum(bmDictData.getOrderNum());
            old.setRemark(bmDictData.getRemark());
            old.setCssClass(bmDictData.getCssClass());
            old.setListClass(bmDictData.getListClass());
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

    private boolean dictTypeUpdateFlag(BmDictType oldObj,BmDictType newObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(oldObj.getDictType());
        sb2.append(newObj.getDictType());
        sb1.append(oldObj.getDictName());
        sb2.append(newObj.getDictName());
        sb1.append(oldObj.getStatus());
        sb2.append(newObj.getStatus());
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
        sb1.append(oldObj.getStatus());
        sb2.append(newObj.getStatus());
        sb1.append(oldObj.getOrderNum());
        sb2.append(newObj.getOrderNum());
        sb1.append(oldObj.getRemark());
        sb2.append(newObj.getRemark());
        sb1.append(oldObj.getCssClass());
        sb2.append(newObj.getCssClass());
        sb1.append(oldObj.getListClass());
        sb2.append(newObj.getListClass());

        return sb1.toString().equals(sb2.toString());
    }


}
