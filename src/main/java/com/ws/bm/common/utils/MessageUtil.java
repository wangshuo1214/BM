package com.ws.bm.common.utils;

import cn.hutool.setting.dialect.Props;

public class MessageUtil {

    private static Props props = new Props("messages.properties","UTF-8");

    public static String getMessage(String key){
        return props.getStr(key);
    }
}
