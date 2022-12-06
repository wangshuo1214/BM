package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("bm_role")
public class BmRole extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String roleId;

    private String roleName;

    private String roleKey;

    private Integer orderNum;

    private Integer menuCheckStrictly;

    private String remark;

    private List<String> menuIds;
}