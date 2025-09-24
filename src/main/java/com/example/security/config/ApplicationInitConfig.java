package com.example.security.config;

import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder encoder;
    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    final String ADMIN_PASSWORD = "admin";
    @NonFinal
    static final String USER_NAME = "user";

    @NonFinal
    final String USER_PASSWORD = "user";
    @NonFinal
    static final String ROLE_ADMIN = "ROLE_ADMIN";
    @NonFinal
    static final String ROLE_USER = "ROLE_USER";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository){
        log.info("Initializing application.....");
        return args -> {
            var roleAdmin = new HashSet<Role>();
            var roleUser = new HashSet<Role>();
            Role a = roleRepository.findRoleByName(ROLE_ADMIN);
            Role u = roleRepository.findRoleByName(ROLE_USER);
            if(a.getName().isEmpty()){
                Role adminRole = roleRepository.save(Role.builder()
                                .name(ROLE_ADMIN)
                                .build());
                roleAdmin.add(adminRole);
            }else {
                roleAdmin.add(a);
            }
            if(u.getName().isEmpty()){
                Role userRole = roleRepository.save(Role.builder()
                                .name(ROLE_ADMIN)
                                .build());
                roleUser.add(userRole);
            }else {
                roleUser.add(u);
            }
            if(userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()){
                User admin = userRepository.save(User.builder()
                                .username(ADMIN_USER_NAME)
                                .fullName(ADMIN_USER_NAME)
                                .roles(roleAdmin)
                                .createdAt(LocalDateTime.now())
                                .password(encoder.encode(ADMIN_PASSWORD))
                                .enabled(true)
                        .build());
            }
            if(userRepository.findByUsername(USER_NAME).isEmpty()){
                User user = userRepository.save(User.builder()
                                .username(USER_NAME)
                                .fullName(USER_NAME)
                                .roles(roleUser)
                                .createdAt(LocalDateTime.now())
                                .password(encoder.encode(USER_PASSWORD))
                                .enabled(true)
                        .build());
            }

            log.info("Application initialization completed .....");
        };
    }

}
