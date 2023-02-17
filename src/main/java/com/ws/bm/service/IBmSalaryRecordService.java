package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmSalaryRecord;

import java.util.List;

public interface IBmSalaryRecordService extends IService<BmSalaryRecord> {

    List<BmSalaryRecord> queryBmSalaryRecord(BmSalaryRecord bmSalaryRecord);

    BmSalaryRecord getBmSalaryRecord(String id);
}
