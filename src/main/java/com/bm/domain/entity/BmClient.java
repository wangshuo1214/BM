package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_client")
public class BmClient extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String clientId;

    private String clientName;

    private String address;

    private String phone;

    private Integer weight;

}
