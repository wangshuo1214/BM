package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_order")
public class BmOrder {

    @TableId(type = IdType.ASSIGN_UUID)
    private String orderId;

    private Date orderDate;

    private String payFlag;

    private BigDecimal payMoney;

    private String remark;

    private Date completedDate;

    private Date updateDate;

    private String deleted;
}
