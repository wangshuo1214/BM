package com.bm.service;

import com.bm.domain.model.LoginBody;

public interface IBmLoginService {

    String login(String username, String password, String code, String uuid);

}
