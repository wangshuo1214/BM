package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BmOrderMapper {

    int addBmOrder(BmOrder bmOrder);

    int batchAddBmOrderDetail(List<BmOrderDetail> bmOrderDetails);

    int batchDeletedBmOrderDetail(String bmOrderId);

    BmOrder getBmOrder(String bmOrderId);

    int updateBmOrder(BmOrder bmOrder);

    List<BmOrderDetail> getBmOrderDetailByOrderId(String bmOrderId);

    List<BmOrder> queryBmOrder(BmOrder bmOrder);

    int deleteBmOrder(List<String> bmOrderIds);

    int deleteBmOrderDetails(List<String> bmOrderIds);

    String getMoneyInfo(String type);
}
