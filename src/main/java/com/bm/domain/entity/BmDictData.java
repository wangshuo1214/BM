package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_dict_data")
public class BmDictData extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String dictCode;

    private String dictName;

    private String dictTypeId;

    private Integer orderNum;

    private String status;

    private String remark;

    private String listClass;

    private String cssClass;
}
