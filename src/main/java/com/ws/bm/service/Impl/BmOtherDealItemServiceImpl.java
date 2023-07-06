package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmClient;
import com.ws.bm.domain.entity.BmOtherDealItem;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmOtherDealItemMapper;
import com.ws.bm.service.IBmOtherDealItemService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BmOtherDealItemServiceImpl extends ServiceImpl<BmOtherDealItemMapper, BmOtherDealItem> implements IBmOtherDealItemService {
    @Override
    public boolean addBmOtherDealItem(BmOtherDealItem bmOtherDealItem) {

        if (checkFiled(bmOtherDealItem)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 名称重复性校验
        QueryWrapper<BmOtherDealItem> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmOtherDealItem::getName,bmOtherDealItem.getName());
        wrapper.lambda().eq(BmOtherDealItem::getType,bmOtherDealItem.getType());
        wrapper.lambda().eq(BmOtherDealItem::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.otherDeal.itemNameRepeat"));
        }
        if (!InitFieldUtil.initField(bmOtherDealItem)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        return save(bmOtherDealItem);
    }

    @Override
    public List<BmOtherDealItem> queryBmOtherDealItem(BmOtherDealItem bmOtherDealItem) {
        if (ObjectUtil.isEmpty(bmOtherDealItem)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmOtherDealItem> wrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(bmOtherDealItem.getType())){
            wrapper.lambda().eq(BmOtherDealItem::getType,bmOtherDealItem.getType());
        }
        if (StrUtil.isNotEmpty(bmOtherDealItem.getName())){
            wrapper.lambda().like(BmOtherDealItem::getName,bmOtherDealItem.getName());
        }
        wrapper.lambda().eq(BmOtherDealItem::getDeleted, BaseConstant.FALSE);
        return list(wrapper);
    }

    @Override
    public boolean deleteBmOtherDealItem(List<String> ids) {
        if (CollUtil.isEmpty(ids)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        List<BmOtherDealItem> delItems = listByIds(ids);
        if (CollUtil.isEmpty(delItems)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        // 修改删除标志 和 修改时间
        for (BmOtherDealItem delItem : delItems) {
            delItem.setDeleted(BaseConstant.TRUE);
            delItem.setUpdateDate(new Date());
        }
        return updateBatchById(delItems);
    }

    @Override
    public boolean updateBmOtherDealItem(BmOtherDealItem bmOtherDealItem) {
        if (ObjectUtil.isEmpty(bmOtherDealItem)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 名称重复性校验
        QueryWrapper<BmOtherDealItem> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmOtherDealItem::getName,bmOtherDealItem.getName());
        wrapper.lambda().eq(BmOtherDealItem::getType,bmOtherDealItem.getType());
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.otherDeal.itemNameRepeat"));
        }
        // 旧对象
        BmOtherDealItem oldItem = getById(bmOtherDealItem.getId());
        if (!updateFlag(bmOtherDealItem,oldItem)){
            oldItem.setName(bmOtherDealItem.getName());
            oldItem.setType(bmOtherDealItem.getType());
            oldItem.setRemark(bmOtherDealItem.getRemark());
        }

        return updateById(oldItem);
    }

    @Override
    public BmOtherDealItem getBmOtherDealItem(String id) {
        if (StrUtil.isEmpty(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmOtherDealItem item = getById(id);
        if (StrUtil.equals(item.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.otherDeal.itemnotexist"));
        }
        return item;
    }

    @Override
    public List<BmOtherDealItem> getBmOtherDealItemByType(String type) {
        if (StrUtil.isEmpty(type)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmOtherDealItem> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmOtherDealItem::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().eq(BmOtherDealItem::getType,type);
        return list(wrapper);
    }

    private boolean checkFiled(BmOtherDealItem bmOtherDealItem){
        if(ObjectUtil.isEmpty(bmOtherDealItem) || StrUtil.isEmpty(bmOtherDealItem.getName()) ||
                StrUtil.isEmpty(bmOtherDealItem.getType())
        ){
            return true;
        }
        return false;
    }

    private boolean updateFlag(BmOtherDealItem newObj, BmOtherDealItem oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getName());
        sb2.append(oldObj.getName());
        sb1.append(newObj.getType());
        sb2.append(oldObj.getType());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());
        return sb1.toString().equals(sb2.toString());
    }
}
