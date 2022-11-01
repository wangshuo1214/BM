package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_log")
public class BmLog {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String title;

    private String businessType;

    private String operId;

    private String operName;

    private String operIp;

    private String deptId;

    private String deptName;
}
