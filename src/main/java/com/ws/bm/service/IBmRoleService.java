package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmRole;

import java.util.List;

public interface IBmRoleService extends IService<BmRole> {

    boolean addBmRole(BmRole role);

    List<BmRole> queryBmRole(BmRole role);

    BmRole getBmRole(String bmRoleId);

    boolean updateBmRole(BmRole role);

    boolean deleteBmRole(List<String> bmRoleIds);
}
