package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmMenu;
import com.ws.bm.domain.entity.BmUser;
import com.ws.bm.domain.model.TreeSelect;

import java.util.List;

public interface IBmMenuService extends IService<BmMenu> {

    boolean addBmMenu(BmMenu bmMenu);

    List<BmMenu> queryBmMenu(BmMenu bmMenu);

    BmMenu getBmMenu(String bmMenuId);

    boolean updBmMenu(BmMenu bmMenu);

    boolean deleteBmMenu(String bmMenuId);

    List<BmMenu> queryBmMenuExcludeChild(String bmMenuId);

    /**构建前端所需要树结构*/
    List<BmMenu> buildMenuTree(List<BmMenu> menus);

    /**构建前端所需要下拉树结构*/
    List<TreeSelect> buildMenuTreeSelect(List<BmMenu> menus);

    /**根据角色ID查询菜单树信息*/
    List<String> selectMenuListByRoleId(String bmRoleId);

    /**
     * 根据用户id查询菜单树
     * @param bmUser
     * @return
     */
    List<BmMenu> queryMenuTreeByUserId(BmUser bmUser);

}
