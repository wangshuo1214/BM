package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class BmUserController extends BaseController{

    @Autowired
    private IBmUserService iBmUserService;

    @PostMapping("/add")
    public Result addBmUser(@RequestBody BmUser bmUser){
        return computeResult(iBmUserService.addBmUser(bmUser));
    }

    @PostMapping("/query")
    public Result queryBmUser(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmUser bmUser = getPageItem(pageQuery,BmUser.class);
        return success(formatTableData(iBmUserService.queryBmUser(bmUser)));
    }

    @PostMapping("/delete")
    public Result deleteBmUser(List<String> bmUserIds){
        return computeResult(iBmUserService.deleteBmUser(bmUserIds));
    }

    @PostMapping("/update")
    public Result updateBmUser(@RequestBody BmUser bmUser){
        return computeResult(iBmUserService.updateBmUser(bmUser));
    }

    @PostMapping("/get")
    public Result getBmUser(String bmUserId){
        return success(iBmUserService.getBmUser(bmUserId));
    }

    @PostMapping("/reset")
    public Result resetBmUserPassword(@RequestBody List<String> bmUserIds){
        return computeResult(iBmUserService.resetBmUserPassword(bmUserIds));
    }
}
