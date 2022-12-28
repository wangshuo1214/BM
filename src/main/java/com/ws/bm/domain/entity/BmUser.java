package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bm_user")
public class BmUser extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    private String deptId;

    @TableField(exist = false)
    private String deptName;

    private String userName;

    private String password;

    private String realName;

    private String status;

    private String remark;
}
