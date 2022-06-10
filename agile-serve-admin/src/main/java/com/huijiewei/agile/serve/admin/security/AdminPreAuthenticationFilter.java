package com.huijiewei.agile.serve.admin.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

public class AdminPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String CLIENT_ID = "X-Client-Id";
    private static final String ACCESS_TOKEN = "X-Access-Token";

    public AdminPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(CLIENT_ID);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return request.getHeader(ACCESS_TOKEN);
    }
}
