package com.ws.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.BmOtherDeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BmOtherDealMapper extends BaseMapper<BmOtherDeal> {

    String getMoneyStatistic(Map<String,String> map);

    String getTotalMoneyStatistic(Map<String,String> map);
}
