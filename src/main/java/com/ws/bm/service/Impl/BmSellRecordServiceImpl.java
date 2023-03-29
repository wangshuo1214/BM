package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.*;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.*;
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

    @Autowired
    BmTransferMapper bmTransferMapper;

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
        bmOrder.setClearFlag(BaseConstant.FALSE);

        // 交易对象的id
        String dealerId = StrUtil.toString(bmOrder.getParams().get("clientId"));
        BmClient bmClient = bmClientMapper.selectById(dealerId);
        if (ObjectUtil.isEmpty(bmClient) || StrUtil.equals(bmClient.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        // 统计销售订单涉及的金额
        BigDecimal debt = BigDecimal.ZERO;
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        for (BmOrderDetail bmOrderDetail : bmOrderDetails) {
            if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) || ObjectUtil.isEmpty(bmOrderDetail.getSort()) ||
                    ObjectUtil.isEmpty(bmOrderDetail.getNum())  || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            if (!InitFieldUtil.initField(bmOrderDetail)){
                throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
            }
            bmOrderDetail.setOrderId(bmOrder.getOrderId());
            bmOrderDetail.setDealerId(dealerId);
            debt = debt.add(bmOrderDetail.getMoney());
        }
        // 增加订单细节
        bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);

        // 修改客户欠款信息
        bmClient.setDebt(bmClient.getDebt().add(debt));
        bmClient.setUpdateDate(new Date());
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
        // 校验交易对象是否已经删除
        BmClient bmClient = bmClientMapper.selectById(dealerId);
        if (ObjectUtil.isEmpty(bmClient) || StrUtil.equals(bmClient.getDeleted(),BaseConstant.TRUE)){
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
                if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) || ObjectUtil.isEmpty(bmOrderDetail.getSort()) ||
                        ObjectUtil.isEmpty(bmOrderDetail.getNum()) || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
                    throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
                }
                if (!InitFieldUtil.initField(bmOrderDetail)){
                    throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
                }
                bmOrderDetail.setOrderId(bmOrder.getOrderId());
                bmOrderDetail.setDealerId(dealerId);
            });
            // 增加订单细节
            bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);

            // 统计新旧订单细节之间的金额差，修改客户的欠款信息
            BigDecimal newMoney = bmOrderDetails.stream().map(a -> a.getMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal oldMoney = oldOrderDetails.stream().map(a -> a.getMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal debtDiff = newMoney.subtract(oldMoney);
            bmClient.setDebt(bmClient.getDebt().add(debtDiff));
            // 如果修改的客户欠款信息小于0就存在问题
            if (bmClient.getDebt().compareTo(BigDecimal.ZERO) < 0){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
            }
            bmClientMapper.updateById(bmClient);

            // 更新订单的最后修改时间
            oldBmOrder.setUpdateDate(new Date());
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
        List<BmOrder> results = bmOrderMapper.querySellBmOrder(bmOrder);
        if (CollUtil.isNotEmpty(results)){
            results.forEach(result -> {
                // 订单细节
                StringBuffer oderDetailName = new StringBuffer("");
                // 订单总销售额
                BigDecimal dealMoney = new BigDecimal(0);
                List<BmOrderDetail> bmOrderDetails = bmOrderMapper.getBmOrderDetailByOrderId(result.getOrderId());
                if (CollUtil.isNotEmpty(bmOrderDetails)){
                    for (int i = 0; i < bmOrderDetails.size(); i++) {
                        BmOrderDetail bmOrderDetail = bmOrderDetails.get(i);
                        dealMoney = dealMoney.add(bmOrderDetail.getMoney());
                        BmMaterial bmMaterial = bmMaterialMapper.selectById(bmOrderDetail.getMaterialId());
                        if (i != bmOrderDetails.size()-1){
                            oderDetailName.append(
                                    bmMaterial.getMaterialName()+ " × " + bmOrderDetail.getNum() + " ( "  +
                                    bmOrderDetail.getMoney().stripTrailingZeros().toPlainString()+"元 ) 、 ");
                        }else {
                            oderDetailName.append(
                                    bmMaterial.getMaterialName()+ " × " + bmOrderDetail.getNum() + " ( " +
                                    bmOrderDetail.getMoney().stripTrailingZeros().toPlainString()+"元 )");
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
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = bmOrderMapper.getBmOrderDetailByOrderId(bmOrderId);
        Map<String,Object> orderDetails = new HashMap();
        orderDetails.put("orderDetails",bmOrderDetails);
        orderDetails.put("clientId",CollUtil.isNotEmpty(bmOrderDetails) ? bmOrderDetails.get(0).getDealerId() : "");
        bmOrder.setParams(orderDetails);
        return bmOrder;
    }

    @Override
    @Transactional
    public int deleteBmSellRecord(List<String> bmOrderIds) {
        if (CollUtil.isEmpty(bmOrderIds)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        List<BmOrderDetail> bmOrderDetailByOrderIds = bmOrderMapper.getBmOrderDetailByOrderIds(bmOrderIds);
        // 删除订单相应的欠款信息
        if (CollUtil.isNotEmpty(bmOrderDetailByOrderIds)){
            BigDecimal delMoney = bmOrderDetailByOrderIds.stream().map(a -> a.getMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 因为删除的话，只会在一个客户处批量删除
            String clientId = bmOrderDetailByOrderIds.get(0).getDealerId();
            BmClient bmClient = bmClientMapper.selectById(clientId);
            bmClient.setDebt(bmClient.getDebt().subtract(delMoney));
            // 如果修改的客户欠款信息小于0就存在问题
            if (bmClient.getDebt().compareTo(BigDecimal.ZERO) < 0){
                throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.sellRecord.clientDebtError"));
            }
            bmClientMapper.updateById(bmClient);
        }
        bmOrderMapper.deleteBmOrderDetails(bmOrderIds);


        return bmOrderMapper.deleteBmOrder(bmOrderIds);
    }

    @Override
    public JSONObject getClientMoenyInfo(String bmClientId) {
        // 获取当前客户的累计欠款
        BmClient bmClient = bmClientMapper.selectById(bmClientId);
        BigDecimal currentDebt = bmClient.getDebt();

        // 获取当前客户累计销售额
        List<BmTransferRecord> transferRecords = bmTransferMapper.getTransferRecordsByClientId(bmClientId);
        BigDecimal totalSellMoney = transferRecords.stream().map(a -> a.getTransferMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentDebt",currentDebt);
        jsonObject.put("totalSellMoney",totalSellMoney);
        return jsonObject;
    }

    /**
     * 付款
     * @param bmTransferRecord
     * @return
     */
    @Override
    public int payMoney(BmTransferRecord bmTransferRecord) {
        if (ObjectUtil.isEmpty(bmTransferRecord) || StrUtil.isEmpty(bmTransferRecord.getClientId()) ||
                ObjectUtil.hasEmpty(bmTransferRecord.getTransferMoney(),bmTransferRecord.getTransferWay(),bmTransferRecord.getTransferDate())
        ){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        if (!InitFieldUtil.initField(bmTransferRecord)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }

        BmClient bmClient = bmClientMapper.selectById(bmTransferRecord.getClientId());
        if (bmTransferRecord.getTransferMoney().compareTo(bmClient.getDebt()) > 0){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.sellRecord.transferOverDebt"));
        }
        bmClient.setDebt(bmClient.getDebt().subtract(bmTransferRecord.getTransferMoney()));
        bmClientMapper.updateById(bmClient);

        return bmTransferMapper.insert(bmTransferRecord);
    }

    /**
     * 清账
     * @param bmClientId
     * @return
     */
    @Override
    public int clearMoney(String bmClientId) {

        // 客户的欠款全部置0
        BmClient bmClient = bmClientMapper.selectById(bmClientId);
        if (ObjectUtil.isEmpty(bmClient) || StrUtil.equals(bmClient.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.client.notexist"));
        }
        bmClient.setDebt(new BigDecimal(0));

        // 修改客户的相关订单清账标志
        bmOrderMapper.updBmOrderClearFlagByClientId(bmClientId);

        return bmClientMapper.updateById(bmClient);
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
