package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_order")
public class BmOrder extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String orderId;

    private String orderName;

    private String orderType;

    private String dealerId;

    private Date orderDate;

    private String payFlag;

    private String remark;

}
