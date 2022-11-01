package com.bm.controller;

import com.bm.common.model.BaseController;
import com.bm.common.model.Result;
import com.bm.domain.model.LoginBody;
import com.bm.service.IBmLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class BmLoginController extends BaseController {

    @Autowired
    private IBmLoginService iBmLoginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody){

        String token = iBmLoginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());

        return error();

    }

}
