package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("bm_product")
public class BmProduct extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String productId;

    private String productName;

    private BigDecimal price;

    private BigDecimal costPrice;

    private BigDecimal wage;

    private Integer weight;

}
