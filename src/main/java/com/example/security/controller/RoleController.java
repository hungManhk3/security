package com.example.security.controller;

import com.example.security.dto.request.RoleRequest;
import com.example.security.dto.response.ApiResponse;
import com.example.security.dto.response.RoleResponse;
import com.example.security.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .content(roleService.createRole(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<RoleResponse>> getRoles(
            @RequestParam(required = false) String q,
            Pageable pageable
    ){
        Page<RoleResponse> page = roleService.getRoles(q, pageable);

        return ApiResponse.<List<RoleResponse>>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
    @GetMapping("/{id}")
    ApiResponse<RoleResponse> getRole(@PathVariable("id") Long id){
        return ApiResponse.<RoleResponse>builder()
                .content(roleService.getRole(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(@PathVariable("id") Long id,@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .content(roleService.updateRole(request,id))
                .build();
    }
    @PatchMapping("/{id}")
    ApiResponse<String> deletedRole(@PathVariable("id") Long id){
        roleService.deleteRole(id);
        return ApiResponse.<String>builder()
                .content("Role has been deleted")
                .build();
    }


}
