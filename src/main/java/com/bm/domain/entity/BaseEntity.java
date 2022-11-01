package com.bm.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    public Date createDate;

    public Date updateDate;

    public String deleted;
}
