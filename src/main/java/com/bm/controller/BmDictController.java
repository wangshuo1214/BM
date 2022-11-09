package com.bm.controller;

import com.bm.common.model.Result;
import com.bm.domain.entity.BmDictType;
import com.bm.service.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result queryBmDictType(@RequestBody BmDictType bmDictType){
        return success(iBmDictService.queryBmDictType(bmDictType));
    }

    @GetMapping("/type/get")
    public Result getBmDictType(String bmDictId){
        return success(iBmDictService.getBmDictType(bmDictId));
    }

    @PostMapping("/type/update")
    public Result updateBmDictType(@RequestBody BmDictType bmDictType){
        int result = iBmDictService.updateBmDictType(bmDictType);
        if (result == -1){
            return success();
        }else {
            return computeResult(result);
        }
    }
}
