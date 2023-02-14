package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmEmployee;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class BmEmployeeController extends BaseController{

    @Autowired
    private IBmEmployeeService iBmEmployeeService;

    @PostMapping("/add")
    public Result addBmEmployee(@RequestBody BmEmployee bmEmployee){
        return computeResult(iBmEmployeeService.addBmEmployee(bmEmployee));
    }

    @PostMapping("/query")
    public Result queryBmEmployee(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmEmployee bmEmployee = getPageItem(pageQuery,BmEmployee.class);
        return success(formatTableData(iBmEmployeeService.queryBmEmployee(bmEmployee)));
    }

    @PostMapping("/delete")
    public Result deleteBmEmployee(@RequestBody List<String> bmEmployeeIds){
        return computeResult(iBmEmployeeService.deleteBmEmployee(bmEmployeeIds));
    }

    @PostMapping("/update")
    public Result updateBmEmployee(@RequestBody BmEmployee bmEmployee){
        return computeResult(iBmEmployeeService.updateBmEmployee(bmEmployee));
    }

    @GetMapping("/get")
    public Result getBmEmployee(String bmEmployeeId){
        return success(iBmEmployeeService.getBmEmployee(bmEmployeeId));
    }

    @GetMapping("/employeeTree")
    public Result getBmEmployeeTree(){
        return success(iBmEmployeeService.getEmployeeTree());
    }

    @GetMapping("/allEmployee")
    public Result getAllEmployees(){
        return success(iBmEmployeeService.getAllEmployees());
    }
}
