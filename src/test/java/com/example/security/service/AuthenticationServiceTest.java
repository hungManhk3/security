package com.example.security.service;

import com.example.security.dto.request.AuthenticationRequest;
import com.example.security.dto.response.AuthenticationResponse;
import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.exception.AppException;
import com.example.security.exception.ErrorCode;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class AuthenticationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void inIt() {
        var roleUser = new HashSet<Role>();
        Role userRole = roleRepository.save(Role.builder()
                .name("ROLE_USER")
                .build());
        roleUser.add(userRole);
        User user = userRepository.save(User.builder()
                .username("user")
                .fullName("user")
                .roles(roleUser)
                .createdAt(LocalDateTime.now())
                .password(encoder.encode("user1234"))
                .enabled(true)
                .build());
        var roleAdmin = new HashSet<Role>();
        Role adminRole = roleRepository.save(Role.builder()
                .name("ROLE_ADMIN")
                .build());
        roleAdmin.add(adminRole);
        User admin = userRepository.save(User.builder()
                .username("admin")
                .fullName("admin")
                .roles(roleAdmin)
                .createdAt(LocalDateTime.now())
                .password(encoder.encode("admin1234"))
                .enabled(true)
                .build());
    }

    @Test
    @DisplayName("Wrong pass")
    @Transactional
    void testAuthenticationWrongPassword(){
        AppException ex = assertThrows(AppException.class,
                () -> authenticationService.authenticate(
                        new AuthenticationRequest("user", "user1234wrong")
                ));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ERROR_PASS);

        log.info("Exception thrown: {}", ex.getMessage());
    }

    @Test
    @DisplayName("200: GET /api/user/profile")
    @Transactional
    void testAuthenticateWithCorrectPassword() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("user", "user1234")
        );
        assertThat(result).isNotNull();
        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getToken()).isNotBlank();

        log.info("AuthenticationResponse: {}", result);

        mockMvc.perform(get("/api/user/profile")
                .header("Authorization", "Bearer " + result.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.username").value("user"))
                .andExpect(jsonPath("$.content.fullName").value("user"));
    }

    @Test
    @DisplayName("401: GET /api/user/profile khi không kèm Auth token")
    @Transactional
    void testAuthenticateWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Authentication required"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"));
    }

    @Test
    @Transactional
    void testUserCannotGetUsers() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("user", "user1234")
        );
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + result.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void testAdminCanGetUsers() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("admin", "admin1234")
        );
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + result.getToken()))
                .andExpect(status().isOk());
    }
}
