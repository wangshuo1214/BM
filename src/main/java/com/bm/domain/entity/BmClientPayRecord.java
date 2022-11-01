package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_client_pay_record")
public class BmClientPayRecord {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String clientId;

    private BigDecimal payAmount;

    private Date payDate;

    private String payWay;
}
