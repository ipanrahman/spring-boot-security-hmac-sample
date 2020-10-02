package com.ipan97.springbootsecurityhmacsample.service;

import com.ipan97.springbootsecurityhmacsample.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> search(Pageable pageable);

    UserDto save(UserDto user);
}
