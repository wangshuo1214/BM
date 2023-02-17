package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmSalaryRecord;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmSalaryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salaryRecord")
public class BmSalaryRecordController extends BaseController{

    @Autowired
    IBmSalaryRecordService iBmSalaryRecordService;

    @PostMapping("/query")
    public Result queryBmSalaryRecord(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmSalaryRecord bmSalaryRecord = getPageItem(pageQuery,BmSalaryRecord.class);
        return success(formatTableData(iBmSalaryRecordService.queryBmSalaryRecord(bmSalaryRecord)));
    }

    @GetMapping("/get")
    public Result getBmSalaryRecord(String id){
        return success(iBmSalaryRecordService.getBmSalaryRecord(id));
    }
}
