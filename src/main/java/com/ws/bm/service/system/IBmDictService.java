package com.ws.bm.service.system;

import com.ws.bm.domain.entity.system.BmDictData;
import com.ws.bm.domain.entity.system.BmDictType;

import java.util.List;

public interface IBmDictService {

    int addBmDictType(BmDictType bmDictType);

    List<BmDictType> queryBmDictType(BmDictType bmDictType);

    int updateBmDictType(BmDictType bmDictType);

    BmDictType getBmDictType(String bmDictId);

    int deleteBmDictType(List<String> bmDictIds);

    int addBmDictData(BmDictData bmDictData);

    int updateBmDictData(BmDictData bmDictData);

    BmDictData getBmDictData(String bmDictId);

    List<BmDictData> queryBmDictData(BmDictData bmDictData);

    int deleteBmDictData(List<String> bmDictIds);

    List<BmDictData> getDictDataByType(String bmDictType);

    BmDictData getSoleDict(String bmDictType, String bmDictCode);
}
