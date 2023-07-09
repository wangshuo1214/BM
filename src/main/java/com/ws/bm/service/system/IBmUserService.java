package com.ws.bm.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.system.BmUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IBmUserService extends IService<BmUser> {

    boolean addBmUser(BmUser bmUser);

    List<BmUser> queryBmUser(BmUser bmUser);

    boolean deleteBmUser(List<String> bmUserIds);

    boolean updateBmUser(BmUser bmUser);

    BmUser getBmUser(String bmUserId);

    boolean resetBmUserPassword(String bmUserId);

    boolean changeBmUserStatus(String bmUserId, String status);

    List<BmUser> queryAllocatedUserList(BmUser bmUser);

    List<BmUser> queryUnAllocatedUserList(BmUser bmUser);

    BmUser getUserProfile(HttpServletRequest request);

    boolean updateUserProfile(BmUser bmUser);
}
