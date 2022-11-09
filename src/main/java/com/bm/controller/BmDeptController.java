package com.bm.controller;

import com.bm.common.model.Result;
import com.bm.domain.entity.BmDept;
import com.bm.service.IBmDeptService;
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
    public Result getBmDept(String deptId){
        return success(iBmDeptService.getBmDept(deptId));
    }

    @PostMapping("/delete")
    public Result deleteBmDept(String deptId){
        return computeResult(iBmDeptService.deleteBmDept(deptId));
    }

    @PostMapping("/update")
    public Result updateBmDept(@RequestBody BmDept bmDept){
        return computeResult(iBmDeptService.updateBmDept(bmDept));
    }

    @PostMapping("/exclude")
    public Result queryBmDeptExcludeChild(String deptId){
        return success(iBmDeptService.queryBmDeptExcludeChild(deptId));
    }
}
