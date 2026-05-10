package com.portfolio.cms.config;

import com.portfolio.cms.entity.UserAccount;
import com.portfolio.cms.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 起動時に管理者アカウントを 1 件投入する。
 *
 * <p>本番デプロイ時は必ず環境変数で初期パスワードを上書きすること:
 * <pre>
 *   INITIAL_ADMIN_USERNAME=admin
 *   INITIAL_ADMIN_PASSWORD=&lt;十分に強いパスワード&gt;
 * </pre>
 *
 * <p>環境変数未指定の場合は、開発用のデフォルト値（{@code admin} / {@code admin123}）を採用するが、
 * 警告ログを出して気付けるようにする。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    @Value("${app.admin.username:" + DEFAULT_ADMIN_USERNAME + "}")
    private String adminUsername;

    @Value("${app.admin.password:" + DEFAULT_ADMIN_PASSWORD + "}")
    private String adminPassword;

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Admin user '{}' already exists; skipping initialization.", adminUsername);
            return;
        }

        UserAccount admin = UserAccount.builder()
                .username(adminUsername)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .build();

        userRepository.save(admin);

        boolean usingDefaultPassword = DEFAULT_ADMIN_PASSWORD.equals(adminPassword);
        if (usingDefaultPassword) {
            log.warn("⚠ Initial admin user created with DEFAULT password. " +
                    "Set environment variable INITIAL_ADMIN_PASSWORD before deploying to production.");
        } else {
            log.info("Initial admin user '{}' created from environment configuration.", adminUsername);
        }
    }
}
