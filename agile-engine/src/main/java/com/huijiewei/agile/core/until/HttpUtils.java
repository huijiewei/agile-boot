package com.huijiewei.agile.core.until;


import jakarta.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

public class HttpUtils {
    private HttpUtils() {
    }

    public static String getUserAgent(final HttpServletRequest servletRequest) {
        return servletRequest.getHeader("User-Agent") != null ? servletRequest.getHeader("User-Agent") : "";
    }

    public static String getClientId(final HttpServletRequest servletRequest) {
        return servletRequest.getHeader("X-Client-Id") != null ? servletRequest.getHeader("X-Client-Id") : "";
    }

    public static String getRemoteAddr(final HttpServletRequest servletRequest) {
        return servletRequest.getRemoteAddr();
    }
}
