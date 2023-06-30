package com.ws.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.BmTransferRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BmTransferRecordMapper extends BaseMapper<BmTransferRecord> {

    List<BmTransferRecord> getTransferRecordsByClientId(String clientId);

    String getSellMoneyStatistic(Map<String,String> map);
}
