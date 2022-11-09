package com.bm.mapper;

import com.bm.domain.entity.BmDictType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BmDictMapper {

    int addBmDictType(BmDictType bmDictType);

    List<BmDictType> queryBmDictType(BmDictType bmDictType);

    List<BmDictType> queryDictByDictType(String dictType);
}
