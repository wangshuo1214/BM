package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_user_role")
public class BmUserRole {

    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    @TableId(type = IdType.ASSIGN_UUID)
    private String roleId;
}
