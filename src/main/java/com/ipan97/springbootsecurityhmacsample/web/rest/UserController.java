package com.ipan97.springbootsecurityhmacsample.web.rest;

import com.ipan97.springbootsecurityhmacsample.service.UserService;
import com.ipan97.springbootsecurityhmacsample.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> search(Pageable pageable) {
        Page<UserDto> users = userService.search(pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> store(@RequestBody UserDto userRequest) {
        UserDto user = userService.save(userRequest);
        return ResponseEntity.ok(user);
    }

}
