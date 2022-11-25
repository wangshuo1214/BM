package com.ws.bm.controller;

import com.ws.bm.domain.model.Result;
import com.ws.bm.domain.entity.BmDept;
import com.ws.bm.service.IBmDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")
public class BmDeptController extends BaseController {

    @Autowired
    private IBmDeptService iBmDeptService;

    @PostMapping("/add")
    public Result addBmDept(@RequestBody BmDept bmDept){
        return computeResult(iBmDeptService.addBmDept(bmDept));
    }

    @PostMapping("/query")
    public Result queryBmDept(@RequestBody BmDept bmDept){
        return success(iBmDeptService.queryBmDept(bmDept));
    }

    @GetMapping("/get")
    public Result getBmDept(String bmDeptId){
        return success(iBmDeptService.getBmDept(bmDeptId));
    }

    @PostMapping("/delete")
    public Result deleteBmDept(String bmDeptId){
        return computeResult(iBmDeptService.deleteBmDept(bmDeptId));
    }

    @PostMapping("/update")
    public Result updateBmDept(@RequestBody BmDept bmDept){
        return computeResult(iBmDeptService.updateBmDept(bmDept));
    }

    @PostMapping("/exclude")
    public Result queryBmDeptExcludeChild(String bmDeptId){
        return success(iBmDeptService.queryBmDeptExcludeChild(bmDeptId));
    }
}
