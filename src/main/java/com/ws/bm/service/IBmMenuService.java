package com.ws.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ws.bm.domain.entity.BmMenu;

import java.util.List;

public interface IBmMenuService extends IService<BmMenu> {

    boolean addBmMenu(BmMenu bmMenu);

    List<BmMenu> queryBmMenu(BmMenu bmMenu);

    BmMenu getBmMenu(String bmMenuId);

    boolean updBmMenu(BmMenu bmMenu);

    boolean deleteBmMenu(String bmMenuId);

    List<BmMenu> queryBmMenuExcludeChild(String bmMenuId);

    List<BmMenu> selectMenuList(BmMenu menu, String userId);

}
