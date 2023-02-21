package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("bm_client_debt")
public class BmClientDebt extends BaseEntity{
    private String id;

    private String clientId;

    private BigDecimal debt;

    private String clearFlag;
}
