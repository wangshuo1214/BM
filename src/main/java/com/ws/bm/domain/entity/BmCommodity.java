package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("bm_commodity")
public class BmCommodity extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String commodityId;

    private String commodityName;

    private String commodityType;//0销售商品  1采购商品

    private Integer weight;

}
