package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmOtherDealItem;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmOtherDealItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/otherDealItem")
public class BmOtherDealItemController extends BaseController{

    @Autowired
    IBmOtherDealItemService iBmOtherDealItemService;

    @PostMapping("/add")
    public Result addBmOtherDealItem(@RequestBody BmOtherDealItem bmOtherDealItem){
        return computeResult(iBmOtherDealItemService.addBmOtherDealItem(bmOtherDealItem));
    }

    @PostMapping("/query")
    public Result queryBmOtherDealItem(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmOtherDealItem bmOtherDealItem = getPageItem(pageQuery,BmOtherDealItem.class);
        return success(formatTableData(iBmOtherDealItemService.queryBmOtherDealItem(bmOtherDealItem)));
    }

    @PostMapping("/delete")
    public Result deleteBmOtherDealItem(@RequestBody List<String> ids){
        return computeResult(iBmOtherDealItemService.deleteBmOtherDealItem(ids));
    }

    @PostMapping("/update")
    public Result updateBmOtherDealItem(@RequestBody BmOtherDealItem bmOtherDealItem){
        return computeResult(iBmOtherDealItemService.updateBmOtherDealItem(bmOtherDealItem));
    }

    @GetMapping("/get")
    public Result getBmOtherDealItem(String id){
        return success(iBmOtherDealItemService.getBmOtherDealItem(id));
    }

    @GetMapping("/getByType")
    public Result getBmOtherDealItemByType(String type){
        return success(iBmOtherDealItemService.getBmOtherDealItemByType(type));
    }
}
