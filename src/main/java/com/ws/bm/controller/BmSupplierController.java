package com.ws.bm.controller;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.domain.entity.BmSupplier;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class BmSupplierController extends BaseController{
    
    @Autowired
    private IBmSupplierService iBmSupplierService;

    @PostMapping("/add")
    public Result addBmSupplier(@RequestBody BmSupplier bmSupplier){
        return computeResult(iBmSupplierService.addBmSupplier(bmSupplier));
    }

    @PostMapping("/query")
    public Result queryBmSupplier(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmSupplier bmSupplier = getPageItem(pageQuery,BmSupplier.class);
        return success(formatTableData(iBmSupplierService.queryBmSupplier(bmSupplier)));
    }

    @PostMapping("/delete")
    public Result deleteBmSupplier(@RequestBody List<String> bmSupplierIds){
        return computeResult(iBmSupplierService.deleteBmSupplier(bmSupplierIds));
    }

    @PostMapping("/update")
    public Result updateBmSupplier(@RequestBody BmSupplier bmSupplier){
        return computeResult(iBmSupplierService.updateBmSupplier(bmSupplier));
    }

    @GetMapping("/get")
    public Result getBmSupplier(String bmSupplierId){
        return success(iBmSupplierService.getBmSupplier(bmSupplierId));
    }

    @GetMapping("/getAll")
    public Result getBmSuppliers(){
        return success(iBmSupplierService.getBmSuppliers());
    }
}
