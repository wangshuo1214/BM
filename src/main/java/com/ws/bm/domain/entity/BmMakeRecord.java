package com.ws.bm.domain.entity;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("bm_make_record")
public class BmMakeRecord extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String employeeId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completeDate;

    private String wageFlag;

    private String salaryId;

    @TableField(exist = false)
    private BigDecimal totalWage;

    @TableField(exist = false)
    private String makeRecordDeatil;
}
