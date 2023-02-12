package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_order_detail")
public class BmOrderDetail extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String materialId;

    private String dealerId;

    private Integer num;

    private BigDecimal money;

    private Integer sort;

    private String orderId;

    private String remark;
}
