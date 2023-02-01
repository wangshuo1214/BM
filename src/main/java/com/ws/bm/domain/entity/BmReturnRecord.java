package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bm_return_record")
public class BmReturnRecord extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String clientId;

    private String materialId;

    private String orderId;

    private Integer num;

    private BigDecimal moeny;

    private Date returnDate;

    private String remark;
}
