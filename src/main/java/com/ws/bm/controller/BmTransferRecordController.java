package com.ws.bm.controller;


import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmTransferRecord;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmTransferRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferRecord")
public class BmTransferRecordController extends BaseController{

    @Autowired
    private IBmTransferRecordService iBmTransferRecordService;

    @PostMapping("/query")
    public Result queryTransferRecord(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmTransferRecord bmTransferRecord = getPageItem(pageQuery,BmTransferRecord.class);
        return success(formatTableData(iBmTransferRecordService.queryBmtransferRecord(bmTransferRecord)));
    }

    @GetMapping("/get")
    public Result getTransferRecord(String id){
        return success(iBmTransferRecordService.getBmTransferRecord(id));
    }
}
