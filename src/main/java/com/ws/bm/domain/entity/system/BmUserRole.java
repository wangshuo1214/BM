package com.ws.bm.domain.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ws.bm.domain.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("bm_user_role")
public class BmUserRole extends BaseEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String userId;

    private String roleId;
}
