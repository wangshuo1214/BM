package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bm_employee_production_record")
public class BmEmployeeProductionRecord {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String workerId;

    private String productId;

    private Integer productNum;

    private Date completedDate;

    private String payWageFlag;

    private String remark;

    private Date updateDate;

    private String deleted;
}
