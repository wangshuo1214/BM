package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_material")
public class BmMaterial extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String materialId;

    private String materialName;

    private String materialType;

    private String remark;

    private Integer sellWeight;

    private Integer buyWeight;

    private Integer makeWeight;
}
