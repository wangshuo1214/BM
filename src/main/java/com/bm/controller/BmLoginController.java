package com.bm.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bm.common.constant.HttpStatus;
import com.bm.common.model.BaseController;
import com.bm.common.model.Result;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmUser;
import com.bm.domain.model.LoginBody;
import com.bm.service.IBmLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class BmLoginController extends BaseController {

    @Autowired
    private IBmLoginService iBmLoginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody){

        String token = iBmLoginService.login(loginBody);

        if(StrUtil.isNotEmpty(token)){
            return success(token);
        }
        return error();
    }

    @GetMapping("/userInfo")
    public Result getUserInfoByToken(String token){

        BmUser bmUser = iBmLoginService.getUserInfoByToken(token);

        if (ObjectUtil.isNotEmpty(bmUser)){
            return success(bmUser);
        }

        return error();
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest httpServletRequest){

        Boolean flag = iBmLoginService.logout(httpServletRequest);

        if (flag){
            return success();
        }

        return error();
    }

}
