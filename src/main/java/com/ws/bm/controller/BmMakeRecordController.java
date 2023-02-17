package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmMakeRecord;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmMakeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/makeRecord")
public class BmMakeRecordController extends BaseController {
    
    @Autowired
    IBmMakeRecordService iBmMakeRecordService;

    @PostMapping("/add")
    public Result addBmMakeRecord(@RequestBody BmMakeRecord bmMakeRecord){
        return computeResult(iBmMakeRecordService.addBmMakeRecord(bmMakeRecord));
    }

    @PostMapping("/query")
    public Result queryBmMakeRecord(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmMakeRecord bmMakeRecord = getPageItem(pageQuery,BmMakeRecord.class);
        return success(formatTableData(iBmMakeRecordService.queryBmMakeRecord(bmMakeRecord)));
    }

    @PostMapping("/delete")
    public Result deleteBmMakeRecord(String id){
        return computeResult(iBmMakeRecordService.deleteBmMakeRecord(id));
    }

    @PostMapping("/update")
    public Result updateBmMakeRecord(@RequestBody BmMakeRecord bmMakeRecord){
        return computeResult(iBmMakeRecordService.updateBmMakeRecord(bmMakeRecord));
    }

    @GetMapping("/get")
    public Result getBmMakeRecord(String id){
        return success(iBmMakeRecordService.getBmMakeRecord(id));
    }


    @PostMapping("/payWage")
    public Result payWage(String employeeId){
        return computeResult(iBmMakeRecordService.payWage(employeeId));
    }

    @GetMapping("/needPay")
    public Result getNeedPayWage(String employeeId){
        return success(iBmMakeRecordService.getNeedPayWage(employeeId));
    }
}
