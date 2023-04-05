package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmOtherMoeny;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmOtherMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/other")
public class BmOtherMoneyController extends BaseController{

    @Autowired
    IBmOtherMoneyService iBmOtherMoneyService;

    @PostMapping("/add")
    public Result addBmOtherMoney(@RequestBody BmOtherMoeny bmOtherMoney){
        return computeResult(iBmOtherMoneyService.addBmOtherMoney(bmOtherMoney));
    }

    @PostMapping("/query")
    public Result queryBmOtherMoney(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmOtherMoeny bmOtherMoeny = getPageItem(pageQuery,BmOtherMoeny.class);
        return success(formatTableData(iBmOtherMoneyService.queryBmOtherMoeny(bmOtherMoeny)));
    }

    @PostMapping("/delete")
    public Result deleteBmOtherMoney(@RequestBody List<String> ids){
        return computeResult(iBmOtherMoneyService.deleteBmOtherMoney(ids));
    }

    @PostMapping("/update")
    public Result updateBmOtherMoney(@RequestBody BmOtherMoeny bmOtherMoeny){
        return computeResult(iBmOtherMoneyService.updateBmOtherMoney(bmOtherMoeny));
    }

    @GetMapping("/get")
    public Result getBmOtherMoney(String id){
        return success(iBmOtherMoneyService.getBmOtherMoney(id));
    }
}
