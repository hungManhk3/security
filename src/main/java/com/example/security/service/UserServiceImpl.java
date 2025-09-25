package com.example.security.service;

import com.example.security.dto.request.PasswordRequest;
import com.example.security.dto.request.RoleRequest;
import com.example.security.dto.request.UserCreateRequest;
import com.example.security.dto.request.UserUpdateRequest;
import com.example.security.dto.response.MessageResponse;
import com.example.security.dto.response.UserResponse;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.exception.AppException;
import com.example.security.exception.ErrorCode;
import com.example.security.mapper.UserMapper;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_EXITED);
        }
        Set<Role> list = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (RoleRequest r : request.getRoles()) {
                Role role = roleRepository.findRoleByName(r.getName());
                list.add(role);
            }
        }
        User user = userMapper.toUser(request);
        user.setRoles(list);
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<UserResponse> getUsers(String kw, Pageable pageable) {
        return userRepository.searchUsers(kw, pageable).map(userMapper::toUserResponse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(UserUpdateRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        Set<Role> list = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (RoleRequest r : request.getRoles()) {
                Role role = roleRepository.findRoleByName(r.getName());
                list.add(role);
            }
        }
        user.getRoles().clear();
        user.setRoles(list);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deletedUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXCEPTION));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER') or #id == authentication.principal.id")
    public void changePassword(Long id, PasswordRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EXCEPTION));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXITED));

        return userMapper.toUserResponse(user);
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public MessageResponse getMsg(String msg) {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return MessageResponse.builder()
                .username(name)
                .msg(msg)
                .build();
    }

}
