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
import com.ws.bm.domain.entity.BmMenu;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.BmMenuMapper;
import com.ws.bm.service.IBmMenuService;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.misc.FieldUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BmMenuServiceImpl extends ServiceImpl<BmMenuMapper, BmMenu> implements IBmMenuService {

    @Override
    public boolean addBmMenu(BmMenu bmMenu) {

        if (checkField(bmMenu)){
            throw new BaseException(HttpStatus.BAD_REQUEST, MessageUtil.getMessage("bm.paramsError"));
        }
        //父菜单
        BmMenu parentMenu = getById(bmMenu.getParentId());

        //如果不在顶级创建 则需要判断父节点状态
        if ( !StrUtil.equals(bmMenu.getParentId(), BaseConstant.FALSE) && (ObjectUtil.isEmpty(parentMenu) || StrUtil.equals(parentMenu.getDeleted(),BaseConstant.TRUE))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.parentStatusError"));
        }

        //判断父节点下菜单是否重复
        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMenu::getParentId,bmMenu.getParentId());
        wrapper.lambda().eq(BmMenu::getMenuName,bmMenu.getMenuName().trim());
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        List<BmMenu> repeatBmMenus = list(wrapper);
        if (CollUtil.isNotEmpty(repeatBmMenus)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.menu.nameRepeat"));
        }

        //初始化基本属性
        if (!InitFieldUtil.initField(bmMenu)){
            throw new BaseException(HttpStatus.ERROR,MessageUtil.getMessage("bm.initFieldError"));
        }

        bmMenu.setMenuId(UUID.randomUUID().toString());
        return save(bmMenu);
    }

    @Override
    public List<BmMenu> queryBmMenu(BmMenu bmMenu) {

        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        if (StrUtil.isNotEmpty(bmMenu.getMenuName())){
            wrapper.lambda().like(BmMenu::getMenuName,bmMenu.getMenuName().trim());
        }
        wrapper.lambda().orderByAsc(BmMenu::getOrderNum).orderByDesc(BmMenu::getCreateDate);
        return list(wrapper);
    }

    @Override
    public BmMenu getBmMenu(String bmMenuId) {

        if (StrUtil.isEmpty(bmMenuId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        BmMenu bmMenu = getById(bmMenuId);
        if (ObjectUtil.isEmpty(bmMenu) || StrUtil.equals(bmMenu.getDeleted(),BaseConstant.TRUE)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.menu.notexist"));
        }
        return bmMenu;
    }

    @Override
    public boolean updBmMenu(BmMenu newBmMenu) {
        if (checkField(newBmMenu)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        //父菜单
        BmMenu parentMenu = getById(newBmMenu.getParentId());

        //判断父节点状态
        if ( !StrUtil.equals(newBmMenu.getParentId(), BaseConstant.FALSE) && (ObjectUtil.isEmpty(parentMenu) || StrUtil.equals(parentMenu.getDeleted(),BaseConstant.TRUE))){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.parentStatusError"));
        }

        //判断父节点下菜单是否重复
        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMenu::getParentId,newBmMenu.getParentId());
        wrapper.lambda().eq(BmMenu::getMenuName,newBmMenu.getMenuName().trim());
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().ne(BmMenu::getMenuId,newBmMenu.getMenuId());
        List<BmMenu> repeatBmMenus = list(wrapper);
        if (CollUtil.isNotEmpty(repeatBmMenus)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.menu.nameRepeat"));
        }

        BmMenu oldBmMenu = getById(newBmMenu.getMenuId());
        if (!updateFlag(newBmMenu,oldBmMenu)){
            oldBmMenu.setMenuName(newBmMenu.getMenuName());
            oldBmMenu.setParentId(newBmMenu.getParentId());
            oldBmMenu.setMenuType(newBmMenu.getMenuType());
            oldBmMenu.setIcon(newBmMenu.getIcon());
            oldBmMenu.setOrderNum(newBmMenu.getOrderNum());
            oldBmMenu.setIsFrame(newBmMenu.getIsFrame());
            oldBmMenu.setPath(newBmMenu.getPath());
            oldBmMenu.setComponent(newBmMenu.getComponent());
            oldBmMenu.setPerms(newBmMenu.getPerms());
            oldBmMenu.setQuery(newBmMenu.getQuery());
            oldBmMenu.setIsCache(newBmMenu.getIsCache());
            oldBmMenu.setVisible(newBmMenu.getVisible());
            oldBmMenu.setUpdateDate(new Date());
        }

        return updateById(oldBmMenu);
    }

    @Override
    public boolean deleteBmMenu(String bmMenuId) {

        if (StrUtil.isEmpty(bmMenuId)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }

        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMenu::getParentId,bmMenuId);
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        List<BmMenu> childList = list(wrapper);

        //有子节点不能删除
        if (CollUtil.isNotEmpty(childList)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.menu.hasChildren"));
        }

        BmMenu bmMenu = getById(bmMenuId);
        bmMenu.setDeleted(BaseConstant.TRUE);
        bmMenu.setUpdateDate(new Date());

        return updateById(bmMenu);
    }

    @Override
    public List<BmMenu> queryBmMenuExcludeChild(String bmMenuId) {
        QueryWrapper<BmMenu> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        //bmMenuId为空认为是点击的添加按钮
        if (StrUtil.isEmpty(bmMenuId)){
            return list(wrapper);
        }else {
            List<String> excludeIds = getChildren(Arrays.asList(bmMenuId));
            if (CollUtil.isNotEmpty(excludeIds)){
                wrapper.lambda().notIn(BmMenu::getMenuId,excludeIds);
            }
            return list(wrapper);
        }
    }

    private List<String> getChildren(List<String> parentIds){
        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(BmMenu::getMenuId);
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        wrapper.lambda().in(BmMenu::getParentId,parentIds);
        List<String> ids = list(wrapper).stream().map(BmMenu::getMenuId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)){
            ids.addAll(getChildren(ids));
            return ids;
        }else {
            return new ArrayList<>();
        }
    }

    private boolean updateFlag(BmMenu newBmMenu, BmMenu oldBmMenu){

        StringBuffer sb1 = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");

        sb1.append(newBmMenu.getMenuName());
        sb2.append(oldBmMenu.getMenuName());
        sb1.append(newBmMenu.getParentId());
        sb2.append(oldBmMenu.getParentId());
        sb1.append(newBmMenu.getMenuType());
        sb2.append(oldBmMenu.getMenuType());
        sb1.append(newBmMenu.getIcon());
        sb2.append(oldBmMenu.getIcon());
        sb1.append(newBmMenu.getOrderNum());
        sb2.append(oldBmMenu.getOrderNum());
        sb1.append(newBmMenu.getIsFrame());
        sb2.append(oldBmMenu.getIsFrame());
        sb1.append(newBmMenu.getPath());
        sb2.append(oldBmMenu.getPath());
        sb1.append(newBmMenu.getComponent());
        sb2.append(oldBmMenu.getComponent());
        sb1.append(newBmMenu.getPerms());
        sb2.append(oldBmMenu.getPerms());
        sb1.append(newBmMenu.getQuery());
        sb2.append(oldBmMenu.getQuery());
        sb1.append(newBmMenu.getIsCache());
        sb2.append(oldBmMenu.getIsCache());
        sb1.append(newBmMenu.getVisible());
        sb2.append(oldBmMenu.getVisible());

        return sb1.toString().equals(sb2.toString());
    }

    private boolean checkField(BmMenu bmMenu){

        //非空校验
        if (ObjectUtil.isEmpty(bmMenu) || StrUtil.isEmpty(bmMenu.getMenuType()) ||
                ((StrUtil.equals(bmMenu.getMenuType(), BaseConstant.CATALOGUE ) || StrUtil.equals(bmMenu.getMenuType(), BaseConstant.MENU)) && (StrUtil.hasEmpty(bmMenu.getMenuName(),bmMenu.getPath(),bmMenu.getIsFrame(),bmMenu.getVisible(),bmMenu.getParentId()) || ObjectUtil.isEmpty(bmMenu.getOrderNum()))) ||
                (StrUtil.equals(bmMenu.getMenuType(), BaseConstant.BUTTON) && (StrUtil.hasEmpty(bmMenu.getMenuName(),bmMenu.getParentId()) || ObjectUtil.isEmpty(bmMenu.getOrderNum())))
        ){
            return true;
        }
        //对菜单类型进行校验,菜单类型只能是 C M B
        if (!StrUtil.equalsAny(bmMenu.getMenuType(),BaseConstant.CATALOGUE,BaseConstant.MENU,BaseConstant.BUTTON)){
            return true;
        }
        //选了目录、菜单、按钮其中之一后，有的选项必须为空，此处进行校验
        if ((StrUtil.equals(bmMenu.getMenuType(), BaseConstant.CATALOGUE) && !StrUtil.hasEmpty(bmMenu.getPath(),bmMenu.getPerms(),bmMenu.getQuery(),bmMenu.getIsCache())) ||
                (StrUtil.equals(bmMenu.getMenuType(), BaseConstant.BUTTON) && !StrUtil.hasEmpty(bmMenu.getIcon(),bmMenu.getIsFrame(),bmMenu.getPath(),bmMenu.getVisible(),bmMenu.getComponent(),bmMenu.getQuery(),bmMenu.getIsCache()))
        ){
            return true;
        }
        return false;
    }
}
