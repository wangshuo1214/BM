package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_client_circular_moeny")
public class BmClientCircularMoney {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String clientId;

    private BigDecimal circularMoney;

    private BigDecimal debt;

    private Date lastCircularDate;
}
