package com.ws.bm.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.system.BmDept;
import com.ws.bm.domain.model.TreeSelect;

import java.util.List;

public interface IBmDeptService extends IService<BmDept> {
    boolean addBmDept(BmDept bmDept);

    List<BmDept> queryBmDept(BmDept bmDept);

    boolean deleteBmDept(String bmDeptId);

    boolean updateBmDept(BmDept bmDept);

    BmDept getBmDept(String bmDeptId);

    List<BmDept> queryBmDeptExcludeChild(String bmDeptId);

    TreeSelect getDeptTree();
}
