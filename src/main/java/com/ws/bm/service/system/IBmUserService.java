package com.ws.bm.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.system.BmUser;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    BmUser getUserProfile(BmUser bmUser);

    boolean updateUserProfile(BmUser bmUser);

    boolean updateUserPwd(BmUser bmUser);

    String uploadAvatar(MultipartFile file, BmUser bmUser);
}
