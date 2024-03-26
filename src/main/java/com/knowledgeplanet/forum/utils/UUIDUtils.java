package com.knowledgeplanet.forum.utils;


import java.util.UUID;


public class UUIDUtils {

    /**
     * 生成UUID，去除 - 连接字符
     * @return 32位没有 - 字符的UUID
     */
    public static String UUID_32 () {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成36位UUID
     */
    public static String UUID_36 () {
        return UUID.randomUUID().toString();
    }
}
