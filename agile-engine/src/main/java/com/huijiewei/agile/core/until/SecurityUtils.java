package com.huijiewei.agile.core.until;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author huijiewei
 */

public class SecurityUtils {
    private final static BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private SecurityUtils() {
    }

    public static String passwordEncode(final String password) {
        return SecurityUtils.PASSWORD_ENCODER.encode(password);
    }

    public static boolean passwordMatches(final String password, final String encodePassword) {
        return SecurityUtils.PASSWORD_ENCODER.matches(password, encodePassword);
    }

    public static String md5(final String value) {
        return DigestUtils.md5Hex(value);
    }
}
