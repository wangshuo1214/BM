package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmMakeRecord;
import com.ws.bm.domain.entity.BmMakeRecordDetail;
import com.ws.bm.domain.entity.BmSalaryRecord;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BmMakeRecordMapper {
    int addBmMakeRecord(BmMakeRecord bmMakeRecord);

    int batchAddBmMakeRecordDetail(List<BmMakeRecordDetail> bmMakeRecordDetails);

    BmMakeRecord getBmMakeRecord(String id);

    List<BmMakeRecordDetail> getBmMakeRecordDetailByMakeRecordId(String makeRecordId);

    int batchDeleteBmMakeRecordDetail(String makeRecordId);

    int updateBmMakeRecord(BmMakeRecord bmMakeRecord);

    List<BmMakeRecord> queryBmMakeRecord(BmMakeRecord bmMakeRecord);

    int deleteBmMakeRecord(String id);

    int deleteBmMakeRecordDetail(String bmMakeRecordId);

    int payWage(List<String> ids);

    String getTotalPayWage(List<String> ids);

    int addBmSalaryRecord(BmSalaryRecord bmSalaryRecord);

    int setSalaryIdForMakeRecord(String salaryId);

    List<BmMakeRecord> getNoPayMakeRecord();

    List<BmMakeRecord> getMakeRecordByIds(List<String> ids);

}
