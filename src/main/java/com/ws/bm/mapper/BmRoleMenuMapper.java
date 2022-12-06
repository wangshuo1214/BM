package com.ws.bm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.bm.domain.entity.BmRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BmRoleMenuMapper extends BaseMapper<BmRoleMenu>{

    /**
     * 批量新增角色菜单
     * @param roleMenuList
     * @return
     */
    int batchAddRoleMenu(List<BmRoleMenu> roleMenuList);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRoleMenuByRoleId(String roleId);
}
