package com.ws.bm.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ws.bm.domain.entity.BmMenu;
import com.ws.bm.domain.model.Result;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.domain.model.LoginBody;
import com.ws.bm.service.IBmLoginService;
import com.ws.bm.service.IBmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping
public class BmLoginController extends BaseController {

    @Autowired
    IBmLoginService iBmLoginService;

    @Autowired
    IBmMenuService iBmMenuService;

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

    @GetMapping("/getRouters")
    public Result GetRouters(){
        return success(iBmMenuService.queryMenuTreeByUserId(getBmUser()));
    }

}
