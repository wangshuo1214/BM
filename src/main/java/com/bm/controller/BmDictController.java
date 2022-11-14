package com.bm.controller;

import com.bm.common.page.PageQuery;
import com.bm.domain.entity.BmDictData;
import com.bm.domain.model.Result;
import com.bm.domain.entity.BmDictType;
import com.bm.service.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/dict")
public class BmDictController extends BaseController {

    @Autowired
    private IBmDictService iBmDictService;

    @PostMapping("/type/add")
    public Result addBmDictType(@RequestBody BmDictType bmDictType){
        return computeResult(iBmDictService.addBmDictType(bmDictType));
    }

    @PostMapping("/type/query")
    public Result queryBmDictType(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmDictType bmDictType = getPageItem(pageQuery,BmDictType.class);
        return success(formatTableData(iBmDictService.queryBmDictType(bmDictType)));
    }

    @GetMapping("/type/list")
    public Result bmDictTypeList(){
        return success(iBmDictService.queryBmDictType(new BmDictType()));
    }

    @GetMapping("/type/get")
    public Result getBmDictType(String bmDictId){
        return success(iBmDictService.getBmDictType(bmDictId));
    }

    @PostMapping("/type/update")
    public Result updateBmDictType(@RequestBody BmDictType bmDictType){
        return computeResult(iBmDictService.updateBmDictType(bmDictType));
    }

    @PostMapping("/type/delete")
    public Result deleteBmDictType(@RequestBody List<String> bmDictIds){
        return computeResult(iBmDictService.deleteBmDictType(bmDictIds));
    }

    @PostMapping("/data/add")
    public Result addBmDictData(@RequestBody BmDictData bmDictData){
        return computeResult(iBmDictService.addBmDictData(bmDictData));
    }

    @PostMapping("/data/update")
    public Result updateBmDictData(@RequestBody BmDictData bmDictData){
        return computeResult(iBmDictService.updateBmDictData(bmDictData));
    }

    @GetMapping("/data/get")
    public Result getBmDictData(String bmDictId){
        return success(iBmDictService.getBmDictData(bmDictId));
    }

    @PostMapping("/data/query")
    public Result queryBmDictData(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmDictData bmDictData = getPageItem(pageQuery,BmDictData.class);
        return success(formatTableData(iBmDictService.queryBmDictData(bmDictData)));
    }

    @PostMapping("/data/delete")
    public Result deleteBmDictData(@RequestBody List<String> bmDictIds){
        return computeResult(iBmDictService.deleteBmDictData(bmDictIds));
    }

    @GetMapping("/data/getDataByType")
    public Result getBmDictDataByType(String bmDictType){
        return success(iBmDictService.getDictDataByType(bmDictType));
    }

}