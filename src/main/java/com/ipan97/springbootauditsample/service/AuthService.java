package com.ipan97.springbootauditsample.service;

import com.ipan97.springbootauditsample.config.ApplicationProperties;
import com.ipan97.springbootauditsample.security.hmac.HmacException;
import com.ipan97.springbootauditsample.security.hmac.HmacToken;
import com.ipan97.springbootauditsample.security.jwt.JwtFilter;
import com.ipan97.springbootauditsample.security.jwt.JwtTokenProvider;
import com.ipan97.springbootauditsample.service.dto.LoginDto;
import com.ipan97.springbootauditsample.service.dto.LoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public static final String JWT_CLAIM_LOGIN = "login";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationProperties applicationProperties;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ApplicationProperties applicationProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationProperties = applicationProperties;
    }

    public LoginResponseDto authenticate(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws HmacException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_CLAIM_LOGIN, loginDto.getUsername());
        claims.put("scope", "READ,WRITE");

        HmacToken hmacToken = jwtTokenProvider.getSignedToken(authentication, claims);

        response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + hmacToken.getJwt());

        long expiresIn = applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();

        return LoginResponseDto.builder()
                .accessToken(hmacToken.getJwt())
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .scope(claims.get("scope").toString())
                .build();

    }
}
