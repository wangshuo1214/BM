package com.ws.bm.controller;

import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmHomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class BmHomePageController extends BaseController{

    @Autowired
    private IBmHomePageService iBmHomePageService;

    @GetMapping("/statistic")
    public Result getHomeStatistic(){
        return success(iBmHomePageService.getHomePageStatistic());
    }
}
