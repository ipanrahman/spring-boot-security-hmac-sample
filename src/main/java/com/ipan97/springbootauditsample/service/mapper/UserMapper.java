package com.ipan97.springbootauditsample.service.mapper;

import com.ipan97.springbootauditsample.domain.User;
import com.ipan97.springbootauditsample.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDto> {
}
