package com.ws.bm.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.system.BmUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BmUserMapper extends BaseMapper<BmUser> {
}
