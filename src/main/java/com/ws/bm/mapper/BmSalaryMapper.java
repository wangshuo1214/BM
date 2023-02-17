package com.ws.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.BmSalaryRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BmSalaryMapper extends BaseMapper<BmSalaryRecord> {
}
