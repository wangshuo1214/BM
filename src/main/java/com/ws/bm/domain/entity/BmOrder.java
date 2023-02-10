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
@TableName("bm_order")
public class BmOrder extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String orderId;

    private String orderType;

    private String dealerId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    private String payFlag;

    private String remark;

    @TableField(exist = false)
    private String dealerName;

    @TableField(exist = false)
    private BigDecimal dealerMoney;

    @TableField(exist = false)
    private String orderDeatil;

}
