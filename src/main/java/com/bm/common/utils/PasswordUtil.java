package com.bm.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import org.springframework.beans.factory.annotation.Value;

public class PasswordUtil {

    @Value("password.prefix")
    private static String passwordPrefix;

    public static String pwdEncrypt(String data){
        if (StrUtil.isEmpty(data)){
            return "";
        }
        return SmUtil.sm3(SecureUtil.md5(passwordPrefix+":"+data));
    }
}
