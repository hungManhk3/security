package com.example.security.dto.response;

import java.time.LocalDateTime;

public interface UserWithRoleProjection {
    Long getUserId();
    String getUsername();
    String getFullName();
    LocalDateTime getCreateAt();
    Boolean getENABLED();
    String getRoleName();
}
