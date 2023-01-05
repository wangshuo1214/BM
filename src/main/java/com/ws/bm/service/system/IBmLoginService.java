package com.ws.bm.service.system;

import com.ws.bm.domain.entity.system.BmUser;
import com.ws.bm.domain.model.LoginBody;

import javax.servlet.http.HttpServletRequest;

public interface IBmLoginService {

    String login(LoginBody loginBody);

    BmUser getUserInfoByToken(String token);

    Boolean logout(HttpServletRequest httpServletRequest);
}
