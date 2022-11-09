package com.bm.service;

import com.bm.domain.entity.BmDictType;

import java.util.List;

public interface IBmDictService {

    int addBmDictType(BmDictType bmDictType);

    List<BmDictType> queryBmDictType(BmDictType bmDictType);

    int updateBmDictType(BmDictType bmDictType);

    BmDictType getBmDictType(String bmDictId);
}
