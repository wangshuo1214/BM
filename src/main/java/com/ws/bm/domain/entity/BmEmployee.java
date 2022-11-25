package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_employee")
public class BmEmployee extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String workerId;

    private String workerName;

    private String phone;
}
