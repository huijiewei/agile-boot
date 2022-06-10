package com.huijiewei.agile.serve.admin.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

public class PreAuthenticationTokenFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String CLIENT_ID = "X-Client-Id";
    private static final String ACCESS_TOKEN = "X-Access-Token";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(CLIENT_ID);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN);
    }
}
