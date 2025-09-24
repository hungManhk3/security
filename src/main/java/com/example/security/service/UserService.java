package com.example.security.service;

import com.example.security.dto.request.PasswordRequest;
import com.example.security.dto.request.UserCreateRequest;
import com.example.security.dto.request.UserUpdateRequest;
import com.example.security.dto.response.MessageResponse;
import com.example.security.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser (UserCreateRequest request);
    Page<UserResponse> getUsers(String kw, Pageable pageable);
    UserResponse getUser(Long id);
    UserResponse updateUser(UserUpdateRequest request, Long id);
    void deletedUser(Long id);
    void changePassword(Long id, PasswordRequest request);
    UserResponse getMyInfo();
    MessageResponse getMsg(String msg);
}
