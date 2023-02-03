package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmOrderDetail;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmBuyRecordMapper;
import com.ws.bm.service.IBmBuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BmBuyRecordServiceImpl implements IBmBuyRecordService {

    @Autowired
    private BmBuyRecordMapper bmBuyRecordMapper;

    @Override
    public int addBuyRecord(BmOrder bmOrder) {
        if (checkFiled(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        if (!InitFieldUtil.initField(bmOrder)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmOrder.setOrderId(UUID.randomUUID().toString());
        // 生成订单名称
        bmOrder.setOrderName(generateOrderName(bmOrder));
        bmOrder.setOrderDate(new Date());
        bmOrder.setPayFlag(BaseConstant.FALSE);
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = (List<BmOrderDetail>) bmOrder.getParams().get("orderDetails");
        bmOrderDetails.forEach(bmOrderDetail -> {
            if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) ||
                    ObjectUtil.isEmpty(bmOrderDetail.getNum()) || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!InitFieldUtil.initField(bmOrder)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmOrderDetail.setOrderId(bmOrder.getOrderId());
        });

        // 增加订单细节
        bmBuyRecordMapper.batchAddBmOrderDetail(bmOrderDetails);

        return bmBuyRecordMapper.addBmOrder(bmOrder);
    }

    private boolean checkFiled(BmOrder bmOrder){
        if(ObjectUtil.isEmpty(bmOrder) || StrUtil.isEmpty(bmOrder.getOrderType()) ||
                StrUtil.isEmpty(bmOrder.getOrderId()) || ObjectUtil.isEmpty(bmOrder.getParams()) ){
            return true;
        }
        List<BmOrderDetail> bmOrderDetails = (List<BmOrderDetail>) bmOrder.getParams().get("orderDetails");
        if ( CollUtil.isEmpty(bmOrderDetails)){
            return true;
        }

        return false;
    }

    //生成订单名称
    private String generateOrderName(BmOrder bmOrder){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatOrderDate = sdf.format(bmOrder.getOrderDate());
        String initOrderName = formatOrderDate + " : " +bmOrder.getDealerId();
        int repeatNum = bmBuyRecordMapper.checkBmOrderNameRepeat(initOrderName);
        String orderName = repeatNum > 0 ? initOrderName += "(" +repeatNum + ")" : initOrderName;
        return orderName;
    }
}
