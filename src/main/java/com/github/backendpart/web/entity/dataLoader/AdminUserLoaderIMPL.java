package com.github.backendpart.web.entity.dataLoader;

import com.github.backendpart.repository.AuthRepository;
import com.github.backendpart.web.entity.enums.Roles;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserLoaderIMPL implements CommandLineRunner {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultUser();
    }

    @Value("${spring.default.admin_id}")
    private String defaultUserId;
    @Value("${spring.default.admin_pwd}")
    private String defaultPassword;

    private void createDefaultUser() {
        String defaultUserName = "Administrator";
        String defaultUserPhone = "123456789";
        String defaultUserAddress = "Admin Address";
        String defaultIsDeleted = "false";

        // Check if the default user already exists
        if (!authRepository.existsByUserId(defaultUserId)) {
            UserEntity defaultUser = UserEntity.builder()
                    .userId(defaultUserId)
                    .userPwd(passwordEncoder.encode(defaultPassword))
                    .userName(defaultUserName)
                    .userPhone(defaultUserPhone)
                    .userAddress(defaultUserAddress)
                    .isDeleted(defaultIsDeleted)
                    .build();

            defaultUser.setRoles(Roles.ROLE_ADMIN);
            log.info("[CreateAdminUser] 관리자 계정이 생성되었습니다.");

            authRepository.save(defaultUser);
        }
        log.info("[CreateAdminUser] 이미 관리자 계정이 존재하여 생성하지 않습니다.");
    }
}
