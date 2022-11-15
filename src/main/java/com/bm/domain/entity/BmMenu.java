package com.bm.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bm_menu")
public class BmMenu extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String menuId;

    private String menuName;

    private String parentId;

    private Integer orderNum;

    private String path;

    private String componet;

    private String query;

    private String isFrame;

    private String isCache;

    private String menuType;

    private String visible;

    private String perms;

    private String icon;

    private String remark;
}
