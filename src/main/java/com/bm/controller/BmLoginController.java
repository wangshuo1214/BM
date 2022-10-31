package com.bm.controller;

import cn.hutool.core.util.StrUtil;
import com.bm.common.domain.BaseController;
import com.bm.common.domain.Result;
import com.bm.entity.BmUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BmLoginController extends BaseController {

    @PostMapping("/login")
    public Result login(@RequestBody BmUser loginUser){

        if(StrUtil.equals("root",loginUser.getUserName()) && StrUtil.equals("123456",loginUser.getPassword())){
            return success();
        }

        return error();

    }

}
