package com.wang.scaffold.files.utils;

import org.apache.commons.lang.StringUtils;

public final class FilenameUtil {

    /**
     * "{", "}", "|", "\", "^", "~", "[", "]", "`"是来自<a href="https://www.ietf.org/rfc/rfc1738.txt">https://www.ietf.org/rfc/rfc1738.txt [page 2]</a>
     * 其他的是文件名不允许或者url中的特殊字符
     */
    private static final String[] unfaceChars = {
            "{", "}", "|", "\\", "^", "~", "[", "]", "`",
            " ", "<", ">", ":", "\"", "/", "?", "*", "&", "#", "="
    };
    private static final String[] replacement = {
            "(", ")", "-", "_", "_", "_", "(", ")", "-",
            "", "(", ")", "-", "_", "-", "_", "_", "_", "_", "_"
    };

    public static String sanitizeName(String originalName) {
        return StringUtils.replaceEach(originalName, unfaceChars, replacement);
    }
}
