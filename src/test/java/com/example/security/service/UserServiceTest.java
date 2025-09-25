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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {
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
    @Transactional
    void testAdminCreateNewUser() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("admin", "admin1234")
        );
        String requestBody = """
            {
                "username": "hulk12",
                "fullName": "N Hulk",
                "password": "hulk1234",
                "roles": [
                    {"name": "ROLE_USER"}
                ]
            }
            """;

        // Gọi API tạo user mới
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        var created = userRepository.findByUsername("hulk12").orElseThrow();
        assertThat(created.getFullName()).isEqualTo("N Hulk");
        assertThat(created.getPassword()).startsWith("$2a$");
//        assertThat(created.getPassword()).startsWith("$2b$");
    }
    @Test
    @Transactional
    void testAdminCreateNewUserSameUserName() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("admin", "admin1234")
        );
        String requestBody = """
            {
                "username": "user",
                "fullName": "N Hulk",
                "password": "hulk1234",
                "roles": [
                    {"name": "ROLE_USER"}
                ]
            }
            """;

        // Gọi API tạo user mới
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1002))
                .andExpect(jsonPath("$.message").value("Username has been exited"));

    }

    @Test
    @Transactional
    void testUpdateRolesAndVerifyAuthorities() throws Exception {
        String requestBody = """
            {
                "username": "user",
                "fullName": "N Hulk",
                "password": "hulk1234",
                "roles": [
                    {"name": "ROLE_ADMIN"}
                ]
            }
            """;
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("admin", "admin1234")
        );
        var user = userRepository.findByUsername("user").orElseThrow();
        mockMvc.perform(put("/api/admin/users/" + user.getId())
                        .header("Authorization", "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + result.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testResetPassWord() throws Exception {
        String requestBody = """
                {
                    "newPassword": "1234user"
                }
            """;
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("admin", "admin1234")
        );
        var user = userRepository.findByUsername("user").orElseThrow();
        mockMvc.perform(post("/api/admin/users/" + user.getId() + "/password")
                        .header("Authorization", "Bearer " + result.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        // test dang nhap van mk cu
        AppException ex = assertThrows(AppException.class,
                () -> authenticationService.authenticate(
                        new AuthenticationRequest(user.getUsername(), user.getPassword())
                ));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ERROR_PASS);

        // test dang nhap ok mk moi
        AuthenticationResponse ok = authenticationService.authenticate(
                new AuthenticationRequest("user", "1234user")
        );
        assertThat(ok).isNotNull();
        assertThat(ok.isAuthenticated()).isTrue();
        assertThat(ok.getToken()).isNotBlank();

    }

    @Test
    @Transactional
    void testJson() throws Exception {
        AuthenticationResponse result = authenticationService.authenticate(
                new AuthenticationRequest("user", "user1234")
        );
        var user = userRepository.findByUsername("user").orElseThrow();
        mockMvc.perform(get("/api/admin/users/" + user.getId())
                        .header("Authorization", "Bearer " + result.getToken()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("Access is denied"))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"));


    }
}
