package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_other_money")
public class BmOtherMoeny extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String otherItem;

    private BigDecimal meony;

    private String type;

    private Date otherDate;

    private String remark;
}
