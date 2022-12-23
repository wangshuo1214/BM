package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmUser;

import java.util.List;

public interface IBmUserService extends IService<BmUser> {

    boolean addBmUser(BmUser bmUser);

    List<BmUser> queryBmUser(BmUser bmUser);

    boolean deleteBmUser(List<String> bmUserIds);

    boolean updateBmUser(BmUser bmUser);

    BmUser getBmUser(String bmUserId);

    boolean resetBmUserPassword(List<String> bmUserIds);
}
