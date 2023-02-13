package com.ws.bm.mapper;

import com.ws.bm.domain.entity.BmMakeRecord;
import com.ws.bm.domain.entity.BmMakeRecordDetail;
import org.apache.ibatis.annotations.Mapper;

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

    int deleteBmMakeRecord(List<String> ids);

    int deleteBmMakeRecordDetail(List<String> bmMakeRecordIds);

    List<BmMakeRecord> getNoPayMakeRecord();

    List<BmMakeRecord> getMakeRecordByIds(List<String> ids);
}
