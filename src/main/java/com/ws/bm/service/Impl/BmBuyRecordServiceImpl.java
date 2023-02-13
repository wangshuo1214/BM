package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmMaterial;
import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmOrderDetail;
import com.ws.bm.domain.entity.BmSupplier;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmOrderMapper;
import com.ws.bm.mapper.BmMaterialMapper;
import com.ws.bm.mapper.BmSupplierMapper;
import com.ws.bm.service.IBmBuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BmBuyRecordServiceImpl implements IBmBuyRecordService {

    @Autowired
    private BmOrderMapper bmOrderMapper;

    @Autowired
    private BmMaterialMapper bmMaterialMapper;

    @Autowired
    private BmSupplierMapper bmSupplierMapper;

    @Override
    @Transactional
    public int addBmBuyRecord(BmOrder bmOrder) {
        if (checkFiled(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (!InitFieldUtil.initField(bmOrder)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmOrder.setOrderId(UUID.randomUUID().toString());
        bmOrder.setOrderType(BaseConstant.BuyOrder);
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        bmOrderDetails.forEach(bmOrderDetail -> {
            if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) || ObjectUtil.isEmpty(bmOrderDetail.getSort()) ||
                    StrUtil.isEmpty(bmOrderDetail.getDealerId()) || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!InitFieldUtil.initField(bmOrderDetail)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmOrderDetail.setOrderId(bmOrder.getOrderId());
        });
        // 增加订单细节
        bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);

        return bmOrderMapper.addBmOrder(bmOrder);
    }

    @Override
    @Transactional
    public int updateBmBuyRecord(BmOrder bmOrder) {
        if(checkFiled(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //旧对象
        BmOrder oldBmOrder = bmOrderMapper.getBmOrder(bmOrder.getOrderId());
        if (ObjectUtil.isEmpty(oldBmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //旧订单细节
        List<BmOrderDetail> oldOrderDetails = bmOrderMapper.getBmOrderDetailByOrderId(bmOrder.getOrderId());
        // 新订单细节
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        if (!StrUtil.equals(bmOrderDetails.toString(),oldOrderDetails.toString())){
            //删除之前的订单细节
            bmOrderMapper.batchDeletedBmOrderDetail(bmOrder.getOrderId());

            bmOrderDetails.forEach(bmOrderDetail -> {
                if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) ||
                        StrUtil.isEmpty(bmOrderDetail.getDealerId()) || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                    throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
                }
                if (!InitFieldUtil.initField(bmOrderDetail)){
                    throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
                }
                bmOrderDetail.setOrderId(bmOrder.getOrderId());
            });
            // 增加订单细节
            bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);
        }
        if (!orderUpdateFlag(bmOrder,oldBmOrder)){
            oldBmOrder.setOrderDate(bmOrder.getOrderDate());
            oldBmOrder.setUpdateDate(new Date());
        }
        return bmOrderMapper.updateBmOrder(oldBmOrder);
    }

    @Override
    public List<BmOrder> queryBmBuyRecord(BmOrder bmOrder) {
        List<BmOrder> results = bmOrderMapper.queryBmOrder(bmOrder);
        if (CollUtil.isNotEmpty(results)){
            results.forEach(result -> {
                StringBuffer oderDetailName = new StringBuffer("");
                BigDecimal dealMoney = new BigDecimal(0);
                List<BmOrderDetail> bmOrderDetails = bmOrderMapper.getBmOrderDetailByOrderId(result.getOrderId());
                if (CollUtil.isNotEmpty(bmOrderDetails)){
                    for (int i = 0; i < bmOrderDetails.size(); i++) {
                        BmOrderDetail bmOrderDetail = bmOrderDetails.get(i);
                        dealMoney = dealMoney.add(bmOrderDetail.getMoney());
                        BmMaterial bmMaterial = bmMaterialMapper.selectById(bmOrderDetail.getMaterialId());
                        if (i != bmOrderDetails.size()-1){
                            oderDetailName.append(bmMaterial.getMaterialName() + "(" +
                                    bmSupplierMapper.selectById(bmOrderDetail.getDealerId()).getSupplierName() + ":" +
                                    bmOrderDetail.getMoney().stripTrailingZeros().toPlainString()+"元) 、 ");
                        }else {
                            oderDetailName.append(bmMaterial.getMaterialName() + "(" +
                                    bmSupplierMapper.selectById(bmOrderDetail.getDealerId()).getSupplierName() + ":" +
                                    bmOrderDetail.getMoney().stripTrailingZeros().toPlainString()+"元)");
                        }
                    }
                    result.setOrderDeatil(oderDetailName.toString());
                    result.setDealerMoney(dealMoney);
                }
            });
        }
        return results;
    }

    @Override
    public BmOrder getBmBuyRecord(String bmOrderId) {
        if (StrUtil.isEmpty(bmOrderId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmOrder bmOrder = bmOrderMapper.getBmOrder(bmOrderId);
        if (ObjectUtil.isEmpty(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        Map<String,Object> orderDetails = new HashMap();
        orderDetails.put("orderDetails",bmOrderMapper.getBmOrderDetailByOrderId(bmOrderId));
        bmOrder.setParams(orderDetails);
        return bmOrder;
    }

    @Override
    @Transactional
    public int deleteBmBuyRecord(List<String> bmOrderIds) {
        if (CollUtil.isEmpty(bmOrderIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmOrderMapper.deleteBmOrderDetails(bmOrderIds);
        return bmOrderMapper.deleteBmOrder(bmOrderIds);
    }

    @Override
    public JSONObject getCostInfo() {
        //获取所有的采购订单中采购的开支
        String totalCostInfo = bmOrderMapper.getCostInfo("total");
        //获取本月采购订单的开支
        String monthCostInfo = bmOrderMapper.getCostInfo("month");
        //获取今日采购订单的开支
        String dayCostInfo = bmOrderMapper.getCostInfo("day");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalCostInfo",StrUtil.isEmpty(totalCostInfo) ? "0" : new BigDecimal(totalCostInfo).stripTrailingZeros().toPlainString());
        jsonObject.put("monthCostInfo",StrUtil.isEmpty(monthCostInfo) ? "0" : new BigDecimal(monthCostInfo).stripTrailingZeros().toPlainString());
        jsonObject.put("dayCostInfo",StrUtil.isEmpty(dayCostInfo) ? "0" : new BigDecimal(dayCostInfo).stripTrailingZeros().toPlainString());
        return jsonObject;
    }

    private boolean orderUpdateFlag(BmOrder newObj, BmOrder oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getOrderDate());
        sb2.append(oldObj.getOrderDate());

        return sb1.toString().equals(sb2.toString());
    }

    private boolean checkFiled(BmOrder bmOrder){
        if(ObjectUtil.isEmpty(bmOrder)  ||
                 ObjectUtil.isEmpty(bmOrder.getOrderDate()) || ObjectUtil.isEmpty(bmOrder.getParams()) ){
            return true;
        }
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        if ( CollUtil.isEmpty(bmOrderDetails)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.buyRecord.deatilNotNull"));
        }

        return false;
    }

}
