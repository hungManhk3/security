package com.example.security.service;

import com.example.security.dto.request.RoleRequest;
import com.example.security.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    Page<RoleResponse> getRoles(String q, Pageable pageable);
    RoleResponse getRole(Long id);
    RoleResponse updateRole(RoleRequest request, Long id);
    void deleteRole(Long id);
}
