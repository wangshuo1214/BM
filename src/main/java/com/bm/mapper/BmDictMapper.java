package com.bm.mapper;

import com.bm.domain.entity.BmDictData;
import com.bm.domain.entity.BmDictType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BmDictMapper {

    int addBmDictType(BmDictType bmDictType);

    List<BmDictType> queryBmDictType(BmDictType bmDictType);

    List<BmDictType> queryDictByDictType(String dictType);

    int updateBmDictType(BmDictType bmDictType);

    BmDictType getBmDictType(String bmDictId);

    int deleteBmDictType(List<String> bmDictIds);

    List<BmDictData> checkBmDictDataUnique(BmDictData bmDictData);

    int addBmDictData(BmDictData bmDictData);

    int updateBmDictData(BmDictData bmDictData);

    BmDictData getBmDictData(String bmDictId);

    List<BmDictData> queryBmDictData(BmDictData bmDictData);

    int deleteBmDictData(List<String> bmDictIds);
}
