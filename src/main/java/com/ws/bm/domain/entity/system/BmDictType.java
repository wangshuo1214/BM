package com.ws.bm.domain.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ws.bm.domain.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("bm_dict_type")
public class BmDictType extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String dictType;

    private String dictName;

    private Integer orderNum;

    private String remark;
}
