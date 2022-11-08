package com.bm.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;

    public String deleted;
}
