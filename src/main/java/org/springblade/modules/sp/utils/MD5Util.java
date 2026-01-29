package org.springblade.modules.sp.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    /**
     * 加密方法
     * @param str
     * @return
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

}
