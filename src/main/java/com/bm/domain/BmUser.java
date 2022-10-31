package com.bm.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bm_user")
public class BmUser {

    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    private String userName;

    private String password;

    private String realName;

    private String status;

    private String loginIp;

    private String remark;

    private Date createDate;

    private Date updateDate;

    private Integer deleted;
}
