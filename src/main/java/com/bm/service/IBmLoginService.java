package com.bm.service;

import com.bm.domain.entity.BmUser;
import com.bm.domain.model.LoginBody;

import javax.servlet.http.HttpServletRequest;

public interface IBmLoginService {

    String login(LoginBody loginBody);

    BmUser getUserInfoByToken(String token);

    Boolean logout(HttpServletRequest httpServletRequest);
}
