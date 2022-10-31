package com.bm.controller;

import com.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class BmUserController {

    @Autowired
    private IBmUserService iBmUserService;
}
