package com.ws.bm.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.domain.entity.BmRole;
import com.ws.bm.domain.entity.BmRoleMenu;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmRoleMapper;
import com.ws.bm.mapper.BmRoleMenuMapper;
import com.ws.bm.service.IBmRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BmRoleServiceImpl extends ServiceImpl<BmRoleMapper, BmRole> implements IBmRoleService {

    @Autowired
    BmRoleMenuMapper bmRoleMenuMapper;

    @Override
    public boolean addBmRole(BmRole role) {
        if (checkField(role)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }

        //检查角色名称是否重复
        QueryWrapper<BmRole> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmRole::getRoleName,role.getRoleName().trim());
        wrapper.lambda().eq(BmRole::getDeleted, BaseConstant.FALSE);
        if (CollUtil.isNotEmpty(list(wrapper))){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.role.nameRepeat"));
        }
        //初始化基本上属性
        if(!InitFieldUtil.initField(role)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }
        role.setRoleId(UUID.randomUUID().toString());

        //插入角色菜单表
        if(CollUtil.isNotEmpty(role.getMenuIds())){
            List<BmRoleMenu> bmRoleMenus = new ArrayList<>();
            role.getMenuIds().forEach(menuId -> {
                BmRoleMenu bmRoleMenu = new BmRoleMenu();
                bmRoleMenu.setRoleId(role.getRoleId());
                bmRoleMenu.setMenuId(menuId);
                bmRoleMenus.add(bmRoleMenu);
            });
            bmRoleMenuMapper.batchAddRoleMenu(bmRoleMenus);
        }
        return save(role);
    }

    @Override
    public List<BmRole> queryBmRole(BmRole role) {
        QueryWrapper<BmRole> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmRole::getDeleted, BaseConstant.FALSE);
        if(StrUtil.isNotEmpty(role.getRoleName())){
            wrapper.lambda().like(BmRole::getRoleName,role.getRoleName().trim());
        }
        if(StrUtil.isNotEmpty(role.getRoleKey())){
            wrapper.lambda().like(BmRole::getRoleKey,role.getRoleKey().trim());
        }
        return list(wrapper);
    }

    @Override
    public BmRole getBmRole(String bmRoleId) {
        if (StrUtil.isEmpty(bmRoleId)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        BmRole bmRole = getById(bmRoleId);

        if (ObjectUtil.isNull(bmRole) || StrUtil.equals(bmRole.getDeleted(), BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.role.notexist"));
        }
        QueryWrapper<BmRoleMenu> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmRoleMenu::getRoleId, bmRoleId);
        List<BmRoleMenu> bmRoles = bmRoleMenuMapper.selectList(wrapper);
        bmRole.setMenuIds(bmRoles.stream().map(BmRoleMenu::getMenuId).collect(Collectors.toList()));
        return bmRole;
    }

    private boolean checkField(BmRole role){
        if (StrUtil.hasEmpty(role.getRoleName(),role.getRoleKey()) ||
                ObjectUtil.hasEmpty(role.getOrderNum(),role.getMenuCheckStrictly())
        ){
            return true;
        }
        return false;
    }

}
