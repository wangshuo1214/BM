package com.ws.bm.service.system.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ws.bm.common.constant.BaseConstant;
import com.ws.bm.common.constant.HttpStatus;
import com.ws.bm.common.utils.InitFieldUtil;
import com.ws.bm.common.utils.MessageUtil;
import com.ws.bm.common.utils.StringUtils;
import com.ws.bm.domain.entity.system.BmMenu;
import com.ws.bm.domain.entity.system.BmRoleMenu;
import com.ws.bm.domain.entity.system.BmUser;
import com.ws.bm.domain.model.MetaVo;
import com.ws.bm.domain.model.RouterVo;
import com.ws.bm.domain.model.TreeSelect;
import com.ws.bm.exception.BaseException;
import com.ws.bm.mapper.system.BmMenuMapper;
import com.ws.bm.mapper.system.BmRoleMenuMapper;
import com.ws.bm.mapper.system.BmUserRoleMapper;
import com.ws.bm.service.system.IBmMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BmMenuServiceImpl extends ServiceImpl<BmMenuMapper, BmMenu> implements IBmMenuService {

    @Autowired
    BmRoleMenuMapper roleMenuMapper;

    @Autowired
    BmUserRoleMapper userRoleMapper;

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

        //有子节点不能删除
        QueryWrapper<BmMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BmMenu::getParentId,bmMenuId);
        wrapper.lambda().eq(BmMenu::getDeleted,BaseConstant.FALSE);
        List<BmMenu> childList = list(wrapper);
        if (CollUtil.isNotEmpty(childList)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.menu.hasChildren"));
        }
        //删除角色和菜单的关联关系
        roleMenuMapper.deleteRoleMenuByMenuId(bmMenuId);

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

    @Override
    public List<BmMenu> buildMenuTree(List<BmMenu> menus) {
        List<BmMenu> returnList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (BmMenu menu : menus) {
            tempList.add(menu.getMenuId());
        }
        for (Iterator<BmMenu> iterator = menus.iterator(); iterator.hasNext();) {
            BmMenu menu = (BmMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<BmMenu> menus) {
        List<BmMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<String> selectMenuListByRoleId(String bmRoleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(bmRoleId);
    }

    @Override
    public List<RouterVo> queryMenuTreeByUserId(BmUser bmUser) {
        if (ObjectUtil.isEmpty(bmUser)){
            throw new BaseException(HttpStatus.BAD_REQUEST,MessageUtil.getMessage("bm.paramsError"));
        }
        List<String> roleIds = userRoleMapper.queryRoleIdsByUserId(bmUser.getUserId());
        if (CollUtil.isNotEmpty(roleIds)){
            List<String> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIds);
            if (CollUtil.isNotEmpty(menuIds)){
                List<BmMenu> bmMenus = listByIds(menuIds);
                bmMenus.stream().filter(bmMenu -> bmMenu.getMenuType() != "B").collect(Collectors.toList());
                if (CollUtil.isNotEmpty(bmMenus)){
                    return buildMenus(buildMenuTree(bmMenus));
                }
            }
        }
        return new ArrayList<>();

    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<BmMenu> list, BmMenu t) {
        // 得到子节点列表
        List<BmMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (BmMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<BmMenu> getChildList(List<BmMenu> list, BmMenu t) {
        List<BmMenu> tlist = new ArrayList<>();
        Iterator<BmMenu> it = list.iterator();
        while (it.hasNext()) {
            BmMenu n = (BmMenu) it.next();
            if (StrUtil.equals(n.getParentId(),t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<BmMenu> list, BmMenu t) {
        return getChildList(list, t).size() > 0;
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
    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<BmMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (BmMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(BaseConstant.FALSE.equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StrUtil.equals("1", menu.getIsCache()), menu.getPath()));
            List<BmMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && BaseConstant.CATALOGUE.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }
    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(BmMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为菜单）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }
    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(BmMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if ( BaseConstant.TOPNODE.equals(menu.getParentId()) && BaseConstant.CATALOGUE.equals(menu.getMenuType())
                && BaseConstant.FALSE.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }
    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(BmMenu menu)
    {
        String component = BaseConstant.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = BaseConstant.PARENT_VIEW;
        }
        return component;
    }
    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(BmMenu menu) {
        return !(BaseConstant.TOPNODE.equals(menu.getParentId())) && BaseConstant.CATALOGUE.equals(menu.getMenuType());
    }
    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(BmMenu menu) {
        return menu.getParentId().equals(BaseConstant.TOPNODE)&& BaseConstant.MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(BaseConstant.FALSE);
    }

}
