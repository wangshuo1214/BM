package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_other_deal_item")
public class BmOtherDealItem extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    private String type;

    private String remark;
}
