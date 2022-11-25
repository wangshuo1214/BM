package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bm_dept")
public class BmDept extends BaseEntity implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    private String deptId;

    private String deptName;

    private String parentId;

    private Integer orderNum;

    private String leader;

    private String phone;

}
