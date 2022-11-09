package com.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bm.common.constant.HttpStatus;
import com.bm.common.utils.InitFieldUtil;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmDictType;
import com.bm.exception.BaseException;
import com.bm.mapper.BmDictMapper;
import com.bm.service.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BmDictServiceImpl implements IBmDictService {

    @Autowired
    private BmDictMapper bmDictMapper;

    @Override
    public int addBmDictType(BmDictType bmDictType) {
        //参数校验
        if (StrUtil.hasEmpty(bmDictType.getDictType(),bmDictType.getDictName(),bmDictType.getStatus()) || ObjectUtil.isEmpty(bmDictType.getOrderNum())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //初始化基本属性
        if (!InitFieldUtil.initField(bmDictType)){
            throw new BaseException(HttpStatus.ERROR, MessageUtil.getMessage("bm.initFieldError"));
        }
        //校验字典类型名称是否重复
        if (CollUtil.isNotEmpty(bmDictMapper.queryDictByDictType(bmDictType.getDictType()))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.dict.nameRepeat"));
        }

        return bmDictMapper.addBmDictType(bmDictType);
    }

    @Override
    public List<BmDictType> queryBmDictType(BmDictType bmDictType) {
        return bmDictMapper.queryBmDictType(bmDictType);
    }
}
