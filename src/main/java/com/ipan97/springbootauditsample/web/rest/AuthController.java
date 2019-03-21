package com.ipan97.springbootauditsample.web.rest;

import com.ipan97.springbootauditsample.security.hmac.HmacException;
import com.ipan97.springbootauditsample.service.AuthService;
import com.ipan97.springbootauditsample.service.dto.LoginDto;
import com.ipan97.springbootauditsample.service.dto.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) throws HmacException {
        LoginResponseDto responseDto = authService.authenticate(loginDto, request, response);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

}
