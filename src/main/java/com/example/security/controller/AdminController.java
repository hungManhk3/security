package com.example.security.controller;

import com.example.security.dto.request.PasswordRequest;
import com.example.security.dto.request.UserCreateRequest;
import com.example.security.dto.request.UserUpdateRequest;
import com.example.security.dto.response.ApiResponse;
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
@RequestMapping("/api/admin/users")
public class AdminController {
    private final UserService userService;
    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .content(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers(
            @RequestParam(required = false) String q,
            Pageable pageable) {
        Page<UserResponse> page = userService.getUsers(q,pageable);

        return ApiResponse.<List<UserResponse>>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable("id") Long id){
        return ApiResponse.<UserResponse>builder()
                .content(userService.getUser(id))
                .build();
    }
    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable("id") Long id){
        return ApiResponse.<UserResponse>builder()
                .content(userService.updateUser(request, id))
                .build();
    }
    @PatchMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable("id") Long id){
        userService.deletedUser(id);
        return ApiResponse.<String>builder()
                .content("User has been deleted")
                .build();
    }
    @PostMapping("/{id}/password")
    ApiResponse<String> changePassword(@PathVariable("id") Long id, @RequestBody PasswordRequest request){
        userService.changePassword(id, request);
        return ApiResponse.<String>builder()
                .content("Password has been change")
                .build();
    }
}
