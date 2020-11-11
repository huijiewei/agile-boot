package com.huijiewei.agile.core.until;

/**
 * @author huijiewei
 */

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isEmpty(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        return org.apache.commons.lang3.StringUtils.isBlank(cs);
    }

    public static String trim(final String str) {
        return org.apache.commons.lang3.StringUtils.trim(str);
    }

    public static boolean contains(final CharSequence seq, final int searchChar) {
        return org.apache.commons.lang3.StringUtils.contains(seq, searchChar);
    }

    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
        return org.apache.commons.lang3.StringUtils.contains(seq, searchSeq);
    }
}
