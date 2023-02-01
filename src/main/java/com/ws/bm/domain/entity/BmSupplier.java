package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_supplier")
public class BmSupplier extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String employeeId;

    private String employeeName;

    private String phone;

    private String address;

    private String remark;

    private Integer weight;
}
