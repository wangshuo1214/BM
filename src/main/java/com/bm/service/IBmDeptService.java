package com.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bm.domain.entity.BmDept;

import java.util.List;

public interface IBmDeptService extends IService<BmDept> {
    boolean addBmDept(BmDept bmDept);

    List<BmDept> queryBmDept(BmDept bmDept);

    boolean deleteBmDept(String bmDeptId);

    boolean updateBmDept(BmDept bmDept);
}
