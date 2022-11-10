package com.bm.common.page;

import lombok.Data;

import java.util.Map;

@Data
public class PageQuery {
    private Map<String,Object> item = null;
    private PageDomain page = null;
}
