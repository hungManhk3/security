package com.example.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 2, max = 100, message = "NAME_VALID")
    private String name;

    public void setName(String name) {
        this.name = (name != null ? name.trim() : null);
    }}
