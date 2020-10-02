package com.ipan97.springbootsecurityhmacsample.security.jwt;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public final class JwtUtils {

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtFilter.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
