package com.ws.bm.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.system.BmRole;

import java.util.List;

public interface IBmRoleService extends IService<BmRole> {

    boolean addBmRole(BmRole role);

    List<BmRole> queryBmRole(BmRole role);

    BmRole getBmRole(String bmRoleId);

    boolean updateBmRole(BmRole role);

    boolean deleteBmRole(List<String> bmRoleIds);

    int allocatedUsers(BmRole bmRole);

    int unAllocatedUsers(BmRole bmRole);
}
