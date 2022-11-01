package com.bm.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bm.common.constant.HttpStatus;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmUser;
import com.bm.exception.UserException;
import com.bm.service.IBmLoginService;
import com.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BmLoginServiceImpl implements IBmLoginService {

    @Autowired
    private IBmUserService iBmUserService;


    @Override
    public String login(String username, String password, String code, String uuid) {

        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getUserName,username);
        BmUser user = iBmUserService.getOne(wrapper);
        if (user == null){
            throw new UserException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.notexist"));
        }
        return null;
    }
}
