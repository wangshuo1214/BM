package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmOrder;
import com.ws.bm.domain.entity.BmTransferRecord;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmSellRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellRecord")
public class BmSellRecordController extends BaseController{

    @Autowired
    private IBmSellRecordService iBmSellRecordService;

    @PostMapping("/add")
    public Result addBmSellRecord(@RequestBody BmOrder bmOrder){
        return computeResult(iBmSellRecordService.addBmSellRecord(bmOrder));
    }

    @PostMapping("/query")
    public Result queryBmSellRecord(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmOrder bmOrder = getPageItem(pageQuery,BmOrder.class);
        return success(formatTableData(iBmSellRecordService.queryBmSellRecord(bmOrder)));
    }

    @PostMapping("/delete")
    public Result deleteBmSellRecord(@RequestBody List<String> bmOrderIds){
        return computeResult(iBmSellRecordService.deleteBmSellRecord(bmOrderIds));
    }

    @PostMapping("/update")
    public Result updateBmSellRecord(@RequestBody BmOrder bmOrder){
        return computeResult(iBmSellRecordService.updateBmSellRecord(bmOrder));
    }

    @GetMapping("/get")
    public Result getBmSellRecord(String bmOrderId){
        return success(iBmSellRecordService.getBmSellRecord(bmOrderId));
    }

    @GetMapping("/sellInfo")
    public Result getCostInfo(String bmClientId){
        return success(iBmSellRecordService.getClientMoenyInfo(bmClientId));
    }

    @PostMapping("/payMoney")
    public Result payMoney(@RequestBody BmTransferRecord bmTransferRecord){
        return computeResult(iBmSellRecordService.payMoney(bmTransferRecord));
    }

    @PostMapping("/clearMoney")
    public Result clearMoney(String bmClientId){
        return computeResult(iBmSellRecordService.clearMoney(bmClientId));
    }
}
