package com.ws.bm.domain.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ws.bm.domain.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("bm_role_menu")
public class BmRoleMenu extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String roleId;

    private String menuId;
}
