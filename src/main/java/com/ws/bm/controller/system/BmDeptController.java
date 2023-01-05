package com.ws.bm.controller.system;

import com.ws.bm.controller.BaseController;
import com.ws.bm.domain.model.Result;
import com.ws.bm.domain.entity.system.BmDept;
import com.ws.bm.domain.model.TreeSelect;
import com.ws.bm.service.system.IBmDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/system/dept")
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

    @GetMapping("/deptTree")
    public Result getDeptTree(){
        List<TreeSelect> result = new ArrayList<>();
        result.add(iBmDeptService.getDeptTree());
        return success(result);
    }
}
