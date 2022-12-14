package com.ws.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;

    public String deleted;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;
}
