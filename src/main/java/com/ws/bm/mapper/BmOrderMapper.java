package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface BmOrderMapper {

    int addBmOrder(BmOrder bmOrder);

    int batchAddBmOrderDetail(List<BmOrderDetail> bmOrderDetails);

    int batchDeletedBmOrderDetail(String bmOrderId);

    BmOrder getBmOrder(String bmOrderId);

    int updateBmOrder(BmOrder bmOrder);

    List<BmOrderDetail> getBmOrderDetailByOrderId(String bmOrderId);

    List<BmOrderDetail> getBmOrderDetailByOrderIds(List<String> bmOrderIds);

    List<BmOrder> queryBuyBmOrder(BmOrder bmOrder);

    List<BmOrder> querySellBmOrder(BmOrder bmOrder);

    int deleteBmOrder(List<String> bmOrderIds);

    int deleteBmOrderDetails(List<String> bmOrderIds);

    String getMoneyInfo(String type);

    int updBmOrderClearFlagByClientId(String bmCLientId);

    String getCostMoneyStatistic(Map<String,String> map);

    List<BmOrderDetail> getBmOrderDetailByUserId(String userId);

}
