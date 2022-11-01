package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_purchase")
public class BmPurchase {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String clientIds;

    private BigDecimal purchaseMoney;

    private Date purchaseDate;

    private String remark;

    private Date updateDate;

    private String deleted;
}
