package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmOtherDeal;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmOtherDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/other")
public class BmOtherDealController extends BaseController{

    @Autowired
    IBmOtherDealService iBmOtherDealService;

    @PostMapping("/add")
    public Result addBmOtherDeal(@RequestBody BmOtherDeal bmOtherDeal){
        return computeResult(iBmOtherDealService.addBmOtherDeal(bmOtherDeal));
    }

    @PostMapping("/query")
    public Result queryBmOtherDeal(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmOtherDeal bmOtherDeal = getPageItem(pageQuery, BmOtherDeal.class);
        return success(formatTableData(iBmOtherDealService.queryBmOtherDeal(bmOtherDeal)));
    }

    @PostMapping("/delete")
    public Result deleteBmOtherDeal(@RequestBody List<String> ids){
        return computeResult(iBmOtherDealService.deleteBmOtherDeal(ids));
    }

    @PostMapping("/update")
    public Result updateBmOtherDeal(@RequestBody BmOtherDeal bmOtherDeal){
        return computeResult(iBmOtherDealService.updateBmOtherDeal(bmOtherDeal));
    }

    @GetMapping("/get")
    public Result getBmOtherDeal(String id){
        return success(iBmOtherDealService.getBmOtherDeal(id));
    }
}
