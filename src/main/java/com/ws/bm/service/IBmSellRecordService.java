package com.ws.bm.service;

import com.alibaba.fastjson.JSONObject;
import com.ws.bm.domain.entity.BmOrder;

import java.util.List;

public interface IBmSellRecordService {
    int addBmSellRecord(BmOrder bmOrder);

    int updateBmSellRecord(BmOrder bmOrder);

    List<BmOrder> queryBmSellRecord(BmOrder bmOrder);

    BmOrder getBmSellRecord(String bmOrderId);

    int deleteBmSellRecord(List<String> bmOrderIds);

    JSONObject getSellInfo();

}
