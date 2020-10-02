package com.ipan97.springbootsecurityhmacsample.security.hmac;

import com.ipan97.springbootsecurityhmacsample.security.WrappedRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class HmacFilter extends GenericFilterBean {

    private final HmacSecurityProvider hmacSecurityProvider;

    public HmacFilter(HmacSecurityProvider hmacSecurityProvider) {
        this.hmacSecurityProvider = hmacSecurityProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        WrappedRequest wrappedRequest = new WrappedRequest(request);
        try {
            if (!request.getRequestURI().contains("/api") || request.getRequestURI().contains("/api/auth/login")) {
                filterChain.doFilter(wrappedRequest, response);
            } else {
                if (hmacSecurityProvider.verifyHmac(wrappedRequest)) {
                    filterChain.doFilter(wrappedRequest, response);
                }
            }

        } catch (Exception e) {
            log.debug("Error while generating hmac token");
            log.trace("Error while generating hmac token {}", e);
            response.setStatus(403);
            response.getWriter().write(e.getMessage());
        }
    }
}
