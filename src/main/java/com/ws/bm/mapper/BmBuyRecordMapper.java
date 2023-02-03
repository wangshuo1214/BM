package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmOrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BmBuyRecordMapper {

    int checkBmOrderNameRepeat(String orderName);

    int addBmOrder(BmOrder bmOrder);

    int batchAddBmOrderDetail(List<BmOrderDetail> bmOrderDetails);
}
