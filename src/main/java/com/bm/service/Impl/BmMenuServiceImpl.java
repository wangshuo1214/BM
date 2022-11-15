package com.bm.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bm.domain.entity.BmMenu;
import com.bm.mapper.BmMenuMapper;
import com.bm.service.IBmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BmMenuServiceImpl extends ServiceImpl<BmMenuMapper, BmMenu> implements IBmMenuService {

    @Autowired
    private IBmMenuService iBmMenuService;

    @Override
    public boolean addBmMenu(BmMenu bmMenu) {
        return false;
    }
}
