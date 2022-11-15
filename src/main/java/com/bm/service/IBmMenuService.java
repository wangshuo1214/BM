package com.bm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bm.domain.entity.BmMenu;

public interface IBmMenuService extends IService<BmMenu> {

    boolean addBmMenu(BmMenu bmMenu);

}
