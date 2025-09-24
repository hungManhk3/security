package com.example.security.mapper;

import com.example.security.dto.request.UserCreateRequest;
import com.example.security.dto.request.UserUpdateRequest;
import com.example.security.dto.response.UserResponse;
import com.example.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
