package com.ws.bm.service;

import com.alibaba.fastjson.JSONObject;
import com.ws.bm.domain.entity.BmMakeRecord;

import java.util.List;

public interface IBmMakeRecordService {

    int addBmMakeRecord(BmMakeRecord bmMakeRecord);

    int updateBmMakeRecord(BmMakeRecord bmMakeRecord);

    List<BmMakeRecord> queryBmMakeRecord(BmMakeRecord bmMakeRecord);

    BmMakeRecord getBmMakeRecord(String id);

    int deleteBmMakeRecord(List<String> ids);

    JSONObject payWage(List<String> ids);
}