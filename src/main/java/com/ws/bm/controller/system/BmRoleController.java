package com.ws.bm.controller.system;

import com.ws.bm.common.page.PageQuery;
import com.ws.bm.controller.BaseController;
import com.ws.bm.domain.entity.system.BmRole;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.system.IBmRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/system/role")
public class BmRoleController extends BaseController {

    @Autowired
    private IBmRoleService iBmRoleService;

    @PostMapping("/add")
    public Result addBmRole(@RequestBody BmRole bmRole){
        return computeResult(iBmRoleService.addBmRole(bmRole));
    }

    @PostMapping("/query")
    public Result queryBmRole(@RequestBody PageQuery pageQuery){
        startPage(pageQuery);
        BmRole bmRole = getPageItem(pageQuery, BmRole.class);
        return success(formatTableData(iBmRoleService.queryBmRole(bmRole)));
    }

    @GetMapping("/get")
    public Result getBmRole(String bmRoleId){
        return success(iBmRoleService.getBmRole(bmRoleId));
    }

    @PostMapping("/update")
    public Result updateBmRole(@RequestBody BmRole role){
        return computeResult(iBmRoleService.updateBmRole(role));
    }

    @PostMapping("/delete")
    public Result deleteBmRole(@RequestBody List<String> bmRoleIds){
        return computeResult(iBmRoleService.deleteBmRole(bmRoleIds));
    }

    @PostMapping("/allocate")
    public Result allocatedUsers(@RequestBody BmRole bmRole){
        return computeResult(iBmRoleService.allocatedUsers(bmRole));
    }

    @PostMapping("/unAllocate")
    public Result unAllocatedUsers(@RequestBody BmRole bmRole){
        return computeResult(iBmRoleService.unAllocatedUsers(bmRole));
    }

}
