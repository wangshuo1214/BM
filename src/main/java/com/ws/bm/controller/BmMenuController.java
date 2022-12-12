package com.ws.bm.controller;

import com.ws.bm.domain.entity.BmMenu;
import com.ws.bm.domain.model.Result;
import com.ws.bm.service.IBmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class BmMenuController extends BaseController{

    @Autowired
    private IBmMenuService iBmMenuService;

    @PostMapping("/add")
    public Result addBmMenu(@RequestBody BmMenu bmMenu){
        return computeResult(iBmMenuService.addBmMenu(bmMenu));
    }

    @PostMapping("/query")
    public Result queryBmMenu(@RequestBody BmMenu bmMenu){
        return success(iBmMenuService.queryBmMenu(bmMenu));
    }

    @GetMapping("/get")
    public Result getBmMenu(String bmMenuId){
        return success(iBmMenuService.getBmMenu(bmMenuId));
    }

    @PostMapping("/update")
    public Result updBmMenu(@RequestBody BmMenu bmMenu){
        return computeResult(iBmMenuService.updBmMenu(bmMenu));
    }

    @PostMapping("/delete")
    public Result deleteBmMenu(String bmMenuId){
        return success(iBmMenuService.deleteBmMenu(bmMenuId));
    }

    @PostMapping("/exclude")
    public Result queryBmMenuExcludeChild(String bmMenuId){
        return success(iBmMenuService.queryBmMenuExcludeChild(bmMenuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public Result treeselect(BmMenu menu)
    {
        List<BmMenu> menus = iBmMenuService.selectMenuList(menu, getUserId());
//        return success(menuService.buildMenuTreeSelect(menus));
        return success();
    }
}
