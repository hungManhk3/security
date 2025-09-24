package com.example.security.service;

import com.example.security.dto.request.RoleRequest;
import com.example.security.dto.response.RoleResponse;
import com.example.security.entity.Role;
import com.example.security.exception.AppException;
import com.example.security.exception.ErrorCode;
import com.example.security.mapper.RoleMapper;
import com.example.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Override
    public RoleResponse createRole(RoleRequest request) {
        if(roleRepository.existsByName(request.getName())){
            throw  new AppException(ErrorCode.USERNAME_EXITED);
        }
        Role role = roleMapper.toRole(request);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public Page<RoleResponse> getRoles(String q, Pageable pageable) {

        return roleRepository.searchRoles(q, pageable).map(roleMapper::toRoleResponse);
    }

    @Override
    public RoleResponse getRole(Long id) {
        return roleMapper.toRoleResponse(roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXCEPTION)));
    }

    @Override
    public RoleResponse updateRole(RoleRequest request, Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXCEPTION));
        role.setName(request.getName());
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
