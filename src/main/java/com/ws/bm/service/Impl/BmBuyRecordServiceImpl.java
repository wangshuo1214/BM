package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        bmOrder.setOrderDate(bmOrder.getOrderDate());
        // 订单细节
        List<BmOrderDetail> bmOrderDetails = JSONObject.parseArray(JSONArray.toJSONString(bmOrder.getParams().get("orderDetails")),BmOrderDetail.class);
        bmOrderDetails.forEach(bmOrderDetail -> {
            if (StrUtil.isEmpty(bmOrderDetail.getMaterialId()) ||
                    ObjectUtil.isEmpty(bmOrderDetail.getNum()) || ObjectUtil.isEmpty(bmOrderDetail.getMoney())){
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
        if (StrUtil.equals(bmOrderDetails.toString(),oldOrderDetails.toString())){
            //删除之前的订单细节
            bmOrderMapper.batchDeletedBmOrderDetail(bmOrder.getOrderId());

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
            bmOrderMapper.batchAddBmOrderDetail(bmOrderDetails);
        }
        if (!orderUpdateFlag(bmOrder,oldBmOrder)){
            oldBmOrder.setDealerId(bmOrder.getDealerId());
            oldBmOrder.setOrderDate(new Date());
            oldBmOrder.setRemark(bmOrder.getRemark());
            oldBmOrder.setUpdateDate(new Date());
        }
        return bmOrderMapper.updateBmOrder(oldBmOrder);
    }

    @Override
    public List<BmOrder> queryBmBuyRecord(BmOrder bmOrder) {
        List<BmOrder> results = bmOrderMapper.queryBmOrder(bmOrder);
        if (CollUtil.isNotEmpty(results)){
            results.forEach(result -> {
                BmSupplier bmSupplier = bmSupplierMapper.selectById(result.getDealerId());
                result.setDealerName(bmSupplier.getSupplierName());
                StringBuffer oderDetailName = new StringBuffer("");
                BigDecimal dealMoney = new BigDecimal(0);
                List<BmOrderDetail> bmOrderDetails = bmOrderMapper.getBmOrderDetailByOrderId(result.getOrderId());
                if (CollUtil.isNotEmpty(bmOrderDetails)){
                    for (int i = 0; i < bmOrderDetails.size(); i++) {
                        BmOrderDetail bmOrderDetail = bmOrderDetails.get(i);
                        dealMoney = dealMoney.add(bmOrderDetail.getMoney());
                        BmMaterial bmMaterial = bmMaterialMapper.selectById(bmOrderDetail.getMaterialId());
                        if (i != bmOrderDetails.size()-1){
                            oderDetailName.append(bmMaterial.getMaterialName() + " × " +bmOrderDetail.getNum()+" 、 ");
                        }else {
                            oderDetailName.append(bmMaterial.getMaterialName() + " × " +bmOrderDetail.getNum());
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
        bmOrder.setOrderDetails(bmOrderMapper.getBmOrderDetailByOrderId(bmOrderId));
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

    private boolean orderUpdateFlag(BmOrder newObj, BmOrder oldObj){
        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");
        sb1.append(newObj.getDealerId());
        sb2.append(oldObj.getDealerId());
        sb1.append(newObj.getOrderDate());
        sb2.append(oldObj.getOrderDate());
        sb1.append(newObj.getRemark());
        sb2.append(oldObj.getRemark());

        return sb1.toString().equals(sb2.toString());
    }

    private boolean checkFiled(BmOrder bmOrder){
        if(ObjectUtil.isEmpty(bmOrder) || StrUtil.isEmpty(bmOrder.getOrderType()) || StrUtil.isEmpty(bmOrder.getDealerId()) ||
                 ObjectUtil.isEmpty(bmOrder.getOrderDate()) || ObjectUtil.isEmpty(bmOrder.getParams()) ){
            return true;
        }
        List<BmOrderDetail> bmOrderDetails = (List<BmOrderDetail>) bmOrder.getParams().get("orderDetails");
        if ( CollUtil.isEmpty(bmOrderDetails)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.buyRecord.deatilNotNull"));
        }

        return false;
    }

}
