package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmBuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyRecord")
public class BmBuyRecordController extends BaseController{
    
    @Autowired
    private IBmBuyRecordService iBmBuyRecordService;

    @PostMapping("/add")
    public Result addBmBuyRecord(@RequestBody BmOrder bmOrder){
        return computeResult(iBmBuyRecordService.addBmBuyRecord(bmOrder));
    }

    @PostMapping("/query")
    public Result queryBmBuyRecord(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmOrder bmOrder = getPageItem(pageQuery,BmOrder.class);
        return success(formatTableData(iBmBuyRecordService.queryBmBuyRecord(bmOrder)));
    }

    @PostMapping("/delete")
    public Result deleteBmBuyRecord(@RequestBody List<String> bmOrderIds){
        return computeResult(iBmBuyRecordService.deleteBmBuyRecord(bmOrderIds));
    }

    @PostMapping("/update")
    public Result updateBmBuyRecord(@RequestBody BmOrder bmOrder){
        return computeResult(iBmBuyRecordService.updateBmBuyRecord(bmOrder));
    }

    @GetMapping("/get")
    public Result getBmBuyRecord(String bmOrderId){
        return success(iBmBuyRecordService.getBmBuyRecord(bmOrderId));
    }

    @GetMapping("/costInfo")
    public Result getCostInfo(){
        return success(iBmBuyRecordService.getCostInfo());
    }
}
