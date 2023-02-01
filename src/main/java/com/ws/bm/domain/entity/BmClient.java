package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("bm_client")
public class BmClient extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String clientId;

    private String clientName;

    private BigDecimal debt;

    private String phone;

    private String address;

    private String remark;

    private Integer weight;

}
