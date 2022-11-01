package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_order_detail")
public class BmOrderDetail extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String orderId;

    private String productId;

    private Integer productNum;

    private BigDecimal costPrice;

    private BigDecimal wage;

    private String clientId;

    private String payWay;

    private String payFlag;

    private BigDecimal payMoeny;

    private Date completedDate;

    private String remark;
}
