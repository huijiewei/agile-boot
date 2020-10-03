package com.huijiewei.agile.core.until;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

public class HttpUtils {
    public static String getUserAgent(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("User-Agent") != null ? servletRequest.getHeader("User-Agent") : "";
    }

    public static String getClientId(HttpServletRequest servletRequest) {
        return servletRequest.getHeader("X-Client-Id") != null ? servletRequest.getHeader("X-Client-Id") : "";
    }

    public static String getRemoteAddr(HttpServletRequest servletRequest) {
        return servletRequest.getRemoteAddr();
    }
}
