package com.ws.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.BmSalaryRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BmSalaryRecordMapper extends BaseMapper<BmSalaryRecord> {

    String getSalaryStatistic(Map<String,String> map);
}
