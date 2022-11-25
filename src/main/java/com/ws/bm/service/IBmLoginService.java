package com.ws.bm.service;

import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.domain.model.LoginBody;

import javax.servlet.http.HttpServletRequest;

public interface IBmLoginService {

    String login(LoginBody loginBody);

    BmUser getUserInfoByToken(String token);

    Boolean logout(HttpServletRequest httpServletRequest);
}
