package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmClient;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class BmClientController extends BaseController{

    @Autowired
    private IBmClientService iBmClientService;

    @PostMapping("/add")
    public Result addBmClient(@RequestBody BmClient bmClient){
        return computeResult(iBmClientService.addBmClient(bmClient));
    }

    @PostMapping("/query")
    public Result queryBmCLient(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmClient bmClient = getPageItem(pageQuery,BmClient.class);
        return success(formatTableData(iBmClientService.queryBmClient(bmClient)));
    }

    @PostMapping("/delete")
    public Result deleteBmClient(@RequestBody List<String> bmClientIds){
        return computeResult(iBmClientService.deleteBmClient(bmClientIds));
    }

    @PostMapping("/update")
    public Result updateBmClient(@RequestBody BmClient bmClient){
        return computeResult(iBmClientService.updateBmClient(bmClient));
    }

    @GetMapping("/get")
    public Result getBmClient(String bmClientId){
        return success(iBmClientService.getBmClient(bmClientId));
    }
}
