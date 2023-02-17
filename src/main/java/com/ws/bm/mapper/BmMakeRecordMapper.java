package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmMakeRecord;
import com.ws.bm.domain.entity.BmMakeRecordDetail;
import com.ws.bm.domain.entity.BmSalaryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    String getTotalPayWage(String employeeId);

    int addBmSalaryRecord(BmSalaryRecord bmSalaryRecord);

    int payWage(@Param("employeeId") String employeeId ,@Param("salaryId") String salaryId);

    List<BmMakeRecord> getEmployeeNoPayMakeRecord(String employeeId);

    List<BmMakeRecord> getMakeRecordBySalaryId(String salaryId);


}
