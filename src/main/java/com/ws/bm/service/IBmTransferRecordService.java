package com.ws.bm.service;

import com.ws.bm.domain.entity.BmTransferRecord;

import java.util.List;

public interface IBmTransferRecordService {

    List<BmTransferRecord> queryBmtransferRecord(BmTransferRecord bmTransferRecord);

    BmTransferRecord getBmTransferRecord(String id);


}
