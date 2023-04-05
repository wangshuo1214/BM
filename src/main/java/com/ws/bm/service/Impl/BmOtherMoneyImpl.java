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
import com.ws.bm.domain.entity.BmOtherMoeny;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmOtherMoneyMapper;
import com.ws.bm.service.IBmOtherMoneyService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BmOtherMoneyImpl extends ServiceImpl<BmOtherMoneyMapper, BmOtherMoeny> implements IBmOtherMoneyService {
    @Override
    public boolean addBmOtherMoney(BmOtherMoeny bmOtherMoeny) {
        if (checkFiled(bmOtherMoeny)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 初始化字段
        if (!InitFieldUtil.initField(bmOtherMoeny)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        return save(bmOtherMoeny);
    }

    @Override
    public List<BmOtherMoeny> queryBmOtherMoeny(BmOtherMoeny bmOtherMoeny) {
        QueryWrapper<BmOtherMoeny> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmOtherMoeny.getType())){
            wrapper.lambda().eq(BmOtherMoeny::getType,bmOtherMoeny.getType());
        }
        if (StrUtil.isNotEmpty(bmOtherMoeny.getItem())){
            wrapper.lambda().eq(BmOtherMoeny::getItem,bmOtherMoeny.getItem());
        }
        if (ObjectUtil.isNotEmpty(bmOtherMoeny.getParams()) &&
                ObjectUtil.isNotEmpty(bmOtherMoeny.getParams().get("date"))){
            Object[] date = (Object[]) bmOtherMoeny.getParams().get("date");
            wrapper.lambda().ge(BmOtherMoeny::getDate,date[0]);
            wrapper.lambda().le(BmOtherMoeny::getDate,date[1]);
        }
        wrapper.lambda().orderByDesc(BmOtherMoeny::getUpdateDate);

        return list(wrapper);
    }

    @Override
    public boolean updateBmOtherMoney(BmOtherMoeny bmOtherMoeny) {
        if (checkFiled(bmOtherMoeny)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        BmOtherMoeny oldObj = getById(bmOtherMoeny.getId());

        if (updateFlag(bmOtherMoeny,oldObj)){
            oldObj.setItem(bmOtherMoeny.getItem());
            oldObj.setMeony(bmOtherMoeny.getMeony());
            oldObj.setDate(bmOtherMoeny.getDate());
        }

        return updateById(oldObj);
    }

    @Override
    public boolean deleteBmOtherMoney(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmOtherMoeny> delBmOtherMoenies = listByIds(ids);
        if (CollUtil.isEmpty(delBmOtherMoenies)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        delBmOtherMoenies.forEach(delBmOtherMoeny -> {
            delBmOtherMoeny.setDeleted(BaseConstant.TRUE);
            delBmOtherMoeny.setUpdateDate(new Date());
        });
        return updateBatchById(delBmOtherMoenies);
    }

    @Override
    public BmOtherMoeny getBmOtherMoney(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmOtherMoeny bmOtherMoeny = getById(id);
        if (ObjectUtil.isEmpty(bmOtherMoeny) || StrUtil.equals(bmOtherMoeny.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmOtherMoeny;
    }

    private boolean checkFiled(BmOtherMoeny bmOtherMoeny){
        if(ObjectUtil.isEmpty(bmOtherMoeny) || StrUtil.isEmpty(bmOtherMoeny.getItem()) ||
                ObjectUtil.isEmpty(bmOtherMoeny.getMeony()) || StrUtil.isEmpty(bmOtherMoeny.getType())
        ){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmOtherMoeny newObj, BmOtherMoeny oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getItem());
        sb2.append(oldObj.getItem());
        sb1.append(newObj.getMeony());
        sb2.append(oldObj.getMeony());
        sb1.append(newObj.getDate());
        sb2.append(oldObj.getDate());
        return sb1.toString().equals(sb2.toString());
    }
}
