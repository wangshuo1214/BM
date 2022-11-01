package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_role_menu")
public class BmRoleMenu {

    @TableId(type = IdType.ASSIGN_UUID)
    private String roleId;

    @TableId(type = IdType.ASSIGN_UUID)
    private String menuId;
}
