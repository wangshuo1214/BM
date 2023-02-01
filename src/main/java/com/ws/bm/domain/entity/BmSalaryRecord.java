package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_salary_record")
public class BmSalaryRecord extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private BigDecimal salary;

    private Date salaryDate;

    private String remark;
}
