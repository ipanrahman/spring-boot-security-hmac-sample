package com.ipan97.springbootauditsample.security.hmac;

import com.ipan97.springbootauditsample.security.jwt.JwtFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class HmacConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final HmacSecurityProvider hmacSecurityProvider;

    public HmacConfigurer(HmacSecurityProvider hmacSecurityProvider) {
        this.hmacSecurityProvider = hmacSecurityProvider;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        HmacFilter hmacFilter = new HmacFilter(hmacSecurityProvider);

        builder.addFilterBefore(hmacFilter, JwtFilter.class);
    }

}
