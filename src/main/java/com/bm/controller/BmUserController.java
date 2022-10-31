package com.bm.controller;

import com.bm.domain.BmUser;
import com.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class BmUserController {

    @Autowired
    private IBmUserService iBmUserService;

    @RequestMapping("/userList")
    public List<BmUser> getUserList(){

        List<BmUser> bmUserList = iBmUserService.list();

        return bmUserList;
    }
}
