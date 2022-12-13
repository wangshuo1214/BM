package com.ws.bm.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ws.bm.domain.entity.BmMenu;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TreeSelect {

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String name;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect(BmMenu menu)
    {
        this.id = menu.getMenuId();
        this.name = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}
