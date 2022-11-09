package com.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bm.domain.entity.BmUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BmUserMapper extends BaseMapper<BmUser> {
}
