package com.huijiewei.agile.core.until;

import com.google.common.net.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public static void setExcelDownload(String fileName, HttpServletResponse servletResponse) {
        servletResponse.setContentType("application/vnd.ms-excel");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"");
    }
}
