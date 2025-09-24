package com.example.security.mapper;

import com.example.security.dto.request.RoleRequest;
import com.example.security.dto.response.RoleResponse;
import com.example.security.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
