package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_transfer_record")
public class BmTransferRecord extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String clientId;

    private BigDecimal transferMoney;

    private String transferWay;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transferDate;

    private String remark;
}
