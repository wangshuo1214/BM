package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_role")
public class BmRole extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String roleId;

    private String roleName;

    private String roleKey;

    private Integer orderNum;

    private String dataScope;

    private Integer menuCheckStrictly;

    private Integer deptCheckStrictly;

    private String status;

    private String remark;
}
