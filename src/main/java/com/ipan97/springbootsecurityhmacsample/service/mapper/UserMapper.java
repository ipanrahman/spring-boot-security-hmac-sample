package com.ipan97.springbootsecurityhmacsample.service.mapper;

import com.ipan97.springbootsecurityhmacsample.domain.User;
import com.ipan97.springbootsecurityhmacsample.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<User, UserDto> {
}
