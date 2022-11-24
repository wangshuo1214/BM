package com.bm.controller;

import com.bm.domain.entity.BmMenu;
import com.bm.domain.model.Result;
import com.bm.service.IBmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class BmMenuController extends BaseController{

    @Autowired
    private IBmMenuService iBmMenuService;

    @PostMapping("/add")
    public Result addBmMenu(@RequestBody BmMenu bmMenu){
        return computeResult(iBmMenuService.addBmMenu(bmMenu));
    }

    @PostMapping("/query")
    public Result queryBmMenu(@RequestBody BmMenu bmMenu){
        return success(iBmMenuService.queryBmMenu(bmMenu));
    }

    @GetMapping("/get")
    public Result getBmMenu(String bmMenuId){
        return success(iBmMenuService.getBmMenu(bmMenuId));
    }

    @PostMapping("/update")
    public Result updBmMenu(@RequestBody BmMenu bmMenu){
        return computeResult(iBmMenuService.updBmMenu(bmMenu));
    }

    @PostMapping("/delete")
    public Result deleteBmMenu(String bmMenuId){
        return success(iBmMenuService.deleteBmMenu(bmMenuId));
    }

    @PostMapping("/exclude")
    public Result queryBmMenuExcludeChild(String bmMenuId){
        return success(iBmMenuService.queryBmMenuExcludeChild(bmMenuId));
    }
}
