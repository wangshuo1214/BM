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
import com.ws.bm.domain.entity.BmOtherDeal;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmOtherDealMapper;
import com.ws.bm.service.IBmOtherDealService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BmOtherDealImpl extends ServiceImpl<BmOtherDealMapper, BmOtherDeal> implements IBmOtherDealService {
    @Override
    public boolean addBmOtherDeal(BmOtherDeal bmOtherDeal) {
        if (checkFiled(bmOtherDeal)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 初始化字段
        if (!InitFieldUtil.initField(bmOtherDeal)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        return save(bmOtherDeal);
    }

    @Override
    public List<BmOtherDeal> queryBmOtherDeal(BmOtherDeal bmOtherDeal) {
        QueryWrapper<BmOtherDeal> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(bmOtherDeal.getType())){
            wrapper.lambda().eq(BmOtherDeal::getType, bmOtherDeal.getType());
        }
        if (StrUtil.isNotEmpty(bmOtherDeal.getDealItem())){
            wrapper.lambda().eq(BmOtherDeal::getDealItem, bmOtherDeal.getDealItem());
        }
        if (ObjectUtil.isNotEmpty(bmOtherDeal.getParams()) &&
                ObjectUtil.isNotEmpty(bmOtherDeal.getParams().get("otherDate"))){
            Object[] date = (Object[]) bmOtherDeal.getParams().get("otherDate");
            wrapper.lambda().ge(BmOtherDeal::getDealDate,date[0]);
            wrapper.lambda().le(BmOtherDeal::getDealDate,date[1]);
        }
        wrapper.lambda().orderByDesc(BmOtherDeal::getUpdateDate);

        return list(wrapper);
    }

    @Override
    public boolean updateBmOtherDeal(BmOtherDeal bmOtherDeal) {
        if (checkFiled(bmOtherDeal)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        BmOtherDeal oldObj = getById(bmOtherDeal.getId());

        if (updateFlag(bmOtherDeal,oldObj)){
            oldObj.setDealItem(bmOtherDeal.getDealItem());
            oldObj.setMoney(bmOtherDeal.getMoney());
            oldObj.setDealDate(bmOtherDeal.getDealDate());
        }

        return updateById(oldObj);
    }

    @Override
    public boolean deleteBmOtherDeal(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmOtherDeal> delBmOtherDeals = listByIds(ids);
        if (CollUtil.isEmpty(delBmOtherDeals)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        delBmOtherDeals.forEach(delBmOtherDeal -> {
            delBmOtherDeal.setDeleted(BaseConstant.TRUE);
            delBmOtherDeal.setUpdateDate(new Date());
        });
        return updateBatchById(delBmOtherDeals);
    }

    @Override
    public BmOtherDeal getBmOtherDeal(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmOtherDeal bmOtherDeal = getById(id);
        if (ObjectUtil.isEmpty(bmOtherDeal) || StrUtil.equals(bmOtherDeal.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        return bmOtherDeal;
    }

    private boolean checkFiled(BmOtherDeal bmOtherDeal){
        if(ObjectUtil.isEmpty(bmOtherDeal) || StrUtil.isEmpty(bmOtherDeal.getDealItem()) ||
                ObjectUtil.isEmpty(bmOtherDeal.getMoney()) || StrUtil.isEmpty(bmOtherDeal.getType())
        ){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmOtherDeal newObj, BmOtherDeal oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getDealItem());
        sb2.append(oldObj.getDealItem());
        sb1.append(newObj.getMoney());
        sb2.append(oldObj.getMoney());
        sb1.append(newObj.getDealDate());
        sb2.append(oldObj.getDealDate());
        return sb1.toString().equals(sb2.toString());
    }
}
