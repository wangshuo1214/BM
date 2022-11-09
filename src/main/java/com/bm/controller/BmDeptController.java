package com.bm.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bm.common.constant.BaseConstant;
import com.bm.common.constant.HttpStatus;
import com.bm.common.model.BaseController;
import com.bm.common.model.Result;
import com.bm.common.utils.InitFieldUtil;
import com.bm.common.utils.MessageUtil;
import com.bm.domain.entity.BmDept;
import com.bm.exception.BaseException;
import com.bm.service.IBmDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
