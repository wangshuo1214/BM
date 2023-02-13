package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("bm_make_record_detail")
public class BmMakeRecordDetail extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String makeRecordId;

    private String materialId;

    private Integer num;

    private BigDecimal wage;

    private Integer sort;

    private String remark;
}
