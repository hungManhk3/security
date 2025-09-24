package com.example.security.controller;

import com.example.security.dto.request.PasswordRequest;
import com.example.security.dto.request.UserCreateRequest;
import com.example.security.dto.request.UserUpdateRequest;
import com.example.security.dto.response.ApiResponse;
import com.example.security.dto.response.MessageResponse;
import com.example.security.dto.response.UserResponse;
import com.example.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .content(userService.getMyInfo())
                .build();
    }

    @GetMapping
    ApiResponse<MessageResponse> getMsg(@RequestParam String msg){
        return  ApiResponse.<MessageResponse>builder()
                .content(userService.getMsg(msg))
                .build();
    }
}
