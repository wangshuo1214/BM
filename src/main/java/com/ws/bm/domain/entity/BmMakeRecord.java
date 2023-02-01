package com.ws.bm.domain.entity;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_make_record")
public class BmMakeRecord {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String employeeId;

    private String materialId;

    private Integer num;

    private BigDecimal wage;

    private Date completeDate;

    private String wageFlag;

    private String salaryId;

    private String remark;
}
