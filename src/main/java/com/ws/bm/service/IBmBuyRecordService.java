package com.ws.bm.service;

import com.alibaba.fastjson.JSONObject;
import com.ws.bm.domain.entity.BmOrder;

import java.util.List;

public interface IBmBuyRecordService {

    int addBmBuyRecord(BmOrder bmOrder);

    int updateBmBuyRecord(BmOrder bmOrder);

    List<BmOrder> queryBmBuyRecord(BmOrder bmOrder);

    BmOrder getBmBuyRecord(String bmOrderId);

    int deleteBmBuyRecord(List<String> bmOrderIds);

    JSONObject getCostInfo();
}
