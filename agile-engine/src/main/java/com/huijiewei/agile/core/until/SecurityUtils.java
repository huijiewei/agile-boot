package com.huijiewei.agile.core.until;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author huijiewei
 */

public class SecurityUtils {
    private final static BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static String passwordEncode(String password) {
        return SecurityUtils.PASSWORD_ENCODER.encode(password);
    }

    public static boolean passwordMatches(String password, String encodePassword) {
        return SecurityUtils.PASSWORD_ENCODER.matches(password, encodePassword);
    }

    public static String md5(String value) {
        return DigestUtils.md5Hex(value);
    }
}
