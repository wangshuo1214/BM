package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("bm_salary_record")
public class BmSalaryRecord extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private BigDecimal salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date salaryDate;

    private String employeeId;

    @TableField(exist = false)
    private String employeeName;

    @TableField(exist = false)
    private List<BmMakeRecord> makeRecords;
}
