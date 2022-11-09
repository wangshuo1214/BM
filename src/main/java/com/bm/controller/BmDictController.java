package com.bm.controller;

import com.bm.common.model.BaseController;
import com.bm.common.model.Result;
import com.bm.domain.entity.BmDictType;
import com.bm.service.IBmDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
