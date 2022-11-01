package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_employee_salary_record")
public class BmEmployeeSalaryRecord {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String workerId;

    private String productionIds;

    private BigDecimal wage;

    private Date wageDate;

    private String remark;

    private Date updateDate;

    private String deleted;
}
