package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmMaterial;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/material")
public class BmMaterialController extends BaseController{
    
    @Autowired
    private IBmMaterialService iBmMaterialService;

    @PostMapping("/add")
    public Result addBmMaterial(@RequestBody BmMaterial bmMaterial){
        return computeResult(iBmMaterialService.addBmMaterial(bmMaterial));
    }

    @PostMapping("/query")
    public Result queryBmMaterial(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmMaterial bmMaterial = getPageItem(pageQuery,BmMaterial.class);
        return success(formatTableData(iBmMaterialService.queryBmMaterial(bmMaterial)));
    }

    @PostMapping("/delete")
    public Result deleteBmMaterial(@RequestBody List<String> bmMaterialIds){
        return computeResult(iBmMaterialService.deleteBmMaterial(bmMaterialIds));
    }

    @PostMapping("/update")
    public Result updateBmMaterial(@RequestBody BmMaterial bmMaterial){
        return computeResult(iBmMaterialService.updateBmMaterial(bmMaterial));
    }

    @GetMapping("/get")
    public Result getBmMaterial(String bmMaterialId){
        return success(iBmMaterialService.getBmMaterial(bmMaterialId));
    }

    @GetMapping("/order")
    public Result getBmMaterialOrderBySell(String orderTag){
        return success(iBmMaterialService.queryBmMaterialOrder(orderTag));
    }
}
