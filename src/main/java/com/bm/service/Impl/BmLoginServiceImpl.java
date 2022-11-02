package com.bm.service.Impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bm.common.constant.HttpStatus;
import com.bm.common.constant.UserConstant;
import com.bm.common.utils.JwtTokenUtil;
import com.bm.common.utils.MessageUtil;
import com.bm.common.utils.PasswordUtil;
import com.bm.common.utils.RedisUtil;
import com.bm.domain.entity.BmUser;
import com.bm.domain.model.LoginBody;
import com.bm.exception.BaseException;
import com.bm.exception.UserException;
import com.bm.service.IBmLoginService;
import com.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BmLoginServiceImpl implements IBmLoginService {

    @Autowired
    private IBmUserService iBmUserService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String login(LoginBody loginBody) {

        if (loginBody == null || StrUtil.hasEmpty(loginBody.getUsername(),loginBody.getPassword())){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        QueryWrapper<BmUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmUser::getUserName,loginBody.getUsername());
        BmUser user = iBmUserService.getOne(wrapper);
        //用户不存在
        if (user == null){
            throw new UserException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.notexist"));
        }
        //密码不正确
        if (StrUtil.equals(PasswordUtil.pwdEncrypt(loginBody.getPassword()),user.getPassword())){
            throw new UserException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.user.passwordError"));
        }
        //用户状态异常（停用）
        if (StrUtil.equals(UserConstant.EXCEPTION,user.getStatus())){
            throw new UserException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.user.statusError"));
        }

        //生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getUserId());
        String token = jwtTokenUtil.createToken(claims);

        //Token放在缓存里,用户每次请求的时候使用
        redisUtil.setCacheObject("bmUserToken"+user.getUserId(),token,15, TimeUnit.MINUTES);
        return token;
    }
}
