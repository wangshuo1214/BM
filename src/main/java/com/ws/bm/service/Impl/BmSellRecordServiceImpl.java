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
import com.ws.bm.domain.entity.*;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmClientMapper;
import com.ws.bm.mapper.BmMaterialMapper;
import com.ws.bm.mapper.BmOrderMapper;
import com.ws.bm.mapper.BmSupplierMapper;
import com.ws.bm.service.IBmSellRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BmSellRecordServiceImpl implements IBmSellRecordService {

    @Autowired
    BmOrderMapper bmOrderMapper;

    @Autowired
    BmMaterialMapper bmMaterialMapper;

    @Autowired
    BmSupplierMapper bmSupplierMapper;

    @Autowired
    BmClientMapper bmClientMapper;

    @Override
    @Transactional
    public int addBmSellRecord(BmOrder bmOrder) {
        if (checkFiled(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (!InitFieldUtil.initField(bmOrder)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        bmOrder.setOrderId(UUID.randomUUID().toString());
        bmOrder.setOrderType(BaseConstant.SellOrder);

        // 交易对象的id
        String dealerId = StrUtil.toString(bmOrder.getParams().get("clientId"));
        // 统计销售订单涉及的金额
        BigDecimal debt = new BigDecimal(0);
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        bmOrderDetails.forEach(bmOrderDetail -> {
            if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) || ObjectUtil.isEmpty(bmOrderDetail.getSort()) ||
                    ObjectUtil.isEmpty(bmOrderDetail.getNum())  || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!InitFieldUtil.initField(bmOrderDetail)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmOrderDetail.setOrderId(bmOrder.getOrderId());
            bmOrderDetail.setDealerId(dealerId);
            debt.add(bmOrderDetail.getMoney());
        });
        // 增加订单细节
        bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);

        // 修改客户欠款信息
        BmClient bmClient = bmClientMapper.selectById(dealerId);
        if (ObjectUtil.isEmpty(bmClient)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmClient.setDebt(bmClient.getDebt().add(debt));
        bmClientMapper.updateById(bmClient);

        return bmOrderMapper.addBmOrder(bmOrder);
    }

    @Override
    @Transactional
    public int updateBmSellRecord(BmOrder bmOrder) {
        if(checkFiled(bmOrder)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        // 交易对象的id
        String dealerId = StrUtil.toString(bmOrder.getParams().get("clientId"));
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
                if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) || ObjectUtil.isEmpty(bmOrderDetail.getSort()) || ObjectUtil.isEmpty(bmOrderDetail.getNum()) ||
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

            // 统计新旧订单细节之间的金额差，修改客户的欠款信息
            BmClient bmClient = bmClientMapper.selectById(dealerId);
            if (ObjectUtil.isEmpty(bmClient)){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!bmClient.getDebt().equals(0)){
                BigDecimal newMoney = bmOrderDetails.stream().map(a -> a.getMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal oldMoney = oldOrderDetails.stream().map(a -> a.getMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal debtDiff = newMoney.subtract(oldMoney);
                bmClient.setDebt(bmClient.getDebt().add(debtDiff));
                bmClientMapper.updateById(bmClient);
            }


        }
        if (!orderUpdateFlag(bmOrder,oldBmOrder)){
            oldBmOrder.setOrderDate(bmOrder.getOrderDate());
            oldBmOrder.setUpdateDate(new Date());
        }
        return bmOrderMapper.updateBmOrder(oldBmOrder);
    }

    @Override
    public List<BmOrder> queryBmSellRecord(BmOrder bmOrder) {
        bmOrder.setOrderType(BaseConstant.SellOrder);
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
                            oderDetailName.append(bmSupplierMapper.selectById(bmOrderDetail.getDealerId()).getSupplierName() + "(" +
                                    bmMaterial.getMaterialName()+ "✖" + bmOrderDetail.getNum() + ":" +
                                    bmOrderDetail.getMoney().stripTrailingZeros().toPlainString()+"元) 、 ");
                        }else {
                            oderDetailName.append(bmSupplierMapper.selectById(bmOrderDetail.getDealerId()).getSupplierName() + "(" +
                                    bmMaterial.getMaterialName()+ "✖" + bmOrderDetail.getNum() + ":" +
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
    public BmOrder getBmSellRecord(String bmOrderId) {
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
    public int deleteBmSellRecord(List<String> bmOrderIds) {
        if (CollUtil.isEmpty(bmOrderIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        bmOrderMapper.deleteBmOrderDetails(bmOrderIds);
        // todo  删除订单相应的欠款信息

        return bmOrderMapper.deleteBmOrder(bmOrderIds);
    }

    @Override
    public JSONObject getSellInfo() {
        //获取所有的销售订单中销售的开支
        String totalCostInfo = bmOrderMapper.getMoneyInfo("total");
        //获取本月销售订单的开支
        String monthCostInfo = bmOrderMapper.getMoneyInfo("month");
        //获取今日销售订单的开支
        String dayCostInfo = bmOrderMapper.getMoneyInfo("day");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalSellInfo",StrUtil.isEmpty(totalCostInfo) ? "0" : new BigDecimal(totalCostInfo).stripTrailingZeros().toPlainString());
        jsonObject.put("monthSellInfo",StrUtil.isEmpty(monthCostInfo) ? "0" : new BigDecimal(monthCostInfo).stripTrailingZeros().toPlainString());
        jsonObject.put("daySellInfo",StrUtil.isEmpty(dayCostInfo) ? "0" : new BigDecimal(dayCostInfo).stripTrailingZeros().toPlainString());
        return jsonObject;
    }

    @Override
    public int payMoney(BmTransferRecord bmTransferRecord) {
        if (ObjectUtil.isEmpty(bmTransferRecord) || StrUtil.isEmpty(bmTransferRecord.getClientId()) ||
                ObjectUtil.hasEmpty(bmTransferRecord.getTransferMoney(),bmTransferRecord.getTransferWay(),bmTransferRecord.getTransferDate())
        ){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        return 0;
    }

    private boolean orderUpdateFlag(BmOrder newObj, BmOrder oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getOrderDate());
        sb2.append(oldObj.getOrderDate());

        return sb1.toString().equals(sb2.toString());
    }

    private boolean checkFiled(BmOrder bmOrder){
        if(ObjectUtil.isEmpty(bmOrder)  || ObjectUtil.isEmpty(bmOrder.getOrderDate()) ||
                ObjectUtil.isEmpty(bmOrder.getParams()) || ObjectUtil.isEmpty(bmOrder.getParams().get("clientId"))){
            return true;
        }
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        if ( CollUtil.isEmpty(bmOrderDetails)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.buyRecord.deatilNotNull"));
        }

        return false;
    }
}
