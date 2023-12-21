package com.huijiewei.agile.core.until;

import com.google.common.net.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public static void setExcelDownload(final String fileName, HttpServletResponse servletResponse) {
        servletResponse.setContentType("application/vnd.ms-excel");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"");
    }
}
