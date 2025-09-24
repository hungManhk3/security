package com.example.security.dto.request;

import com.example.security.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "USERNAME_VALID")
    @Size(min = 3, max = 32, message = "USERNAME_VALID")
    private String username;
    @NotBlank(message = "PASS_VALID")
    @Size(min = 8, message = "PASS_VALID")
    private String password;
    private String fullName;
    private boolean enabled = true;
    private Set<RoleRequest> roles = new HashSet<>();
}
