package com.ws.bm.mapper.system;

import com.ws.bm.domain.entity.system.BmRole;
import com.ws.bm.domain.entity.system.BmUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BmUserRoleMapper {

    /**
     * 根据角色id查询用户角色关联关系
     * @param roleIds
     * @return
     */
    List<BmUserRole> queryUserRolesByRoleIds(List<String> roleIds);

    /**
     * 根据用户id查询用户角色关联关系
     * @param userIds
     * @return
     */
    List<BmUserRole> queryUserRolesByUserIds(List<String> userIds);

    /**
     * 根据角色id查询关联用户id集合
     * @param roleId
     * @return
     */
    List<String> queryUserIdsByRoleId(String roleId);

    /**
     * 根据用户id获取角色id集合
     * @param userId
     * @return
     */
    List<String> queryRoleIdsByUserId(String userId);

    /**
     * 角色授权用户
     * @param bmUserRoles
     * @return
     */
    int allocateUsers(List<BmUserRole> bmUserRoles);

    /**
     * 取消角色授权
     * @param bmRole
     * @return
     */
    int unAllocateUsers(BmRole bmRole);
}
