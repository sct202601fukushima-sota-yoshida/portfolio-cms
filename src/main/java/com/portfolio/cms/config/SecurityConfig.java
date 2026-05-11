package com.portfolio.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
public class SecurityConfig {

    /**
     * Content Security Policy.
     *
     * <p>方針:
     * <ul>
     *   <li>原則: {@code 'self'} のみを許可し、外部リソース読込を遮断（CDN/Analytics 等を一切使っていない設計）</li>
     *   <li>例外: {@code script-src} と {@code style-src} に {@code 'unsafe-inline'} を許可する。
     *       理由: <code>&lt;script type="application/ld+json"&gt;</code> による構造化データと、
     *       公開LP の小規模なインライン JS (TOC / lightbox / scroll-to-top) を許容するため。
     *       Markdown 本文の HTML エスケープは {@link com.portfolio.cms.service.MarkdownService} 側で
     *       既に閉じている（{@code escapeHtml(true)}）ので、ここを開けても XSS 経路は残らない</li>
     *   <li>{@code frame-ancestors 'none'}: クリックジャッキング対策</li>
     *   <li>{@code form-action 'self'}: フォーム送信先を同一オリジンに制限</li>
     *   <li>{@code upgrade-insecure-requests}: HTTP 残存リソースを HTTPS へ自動昇格</li>
     * </ul>
     */
    private static final String CSP =
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: blob:; " +
            "font-src 'self'; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'; " +
            "base-uri 'self'; " +
            "form-action 'self'; " +
            "object-src 'none'; " +
            "upgrade-insecure-requests";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/uploads/**", "/admin/login",
                                 "/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .headers(headers -> headers
                // CSP — XSS / clickjacking / 外部リソース読込を構造的に遮断
                .contentSecurityPolicy(csp -> csp.policyDirectives(CSP))
                // HSTS — Render の前段で HTTPS 終端されるため、includeSubDomains で onrender.com 全体を対象に
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31_536_000) // 1 year
                )
                // X-Frame-Options: DENY — 任意のサイトからの iframe 埋め込みを禁止 (clickjacking 対策)
                // CSP の frame-ancestors と併せた二重防御
                .frameOptions(frame -> frame.deny())
                // X-Content-Type-Options: nosniff — Content-Type の MIME sniffing を抑制
                .contentTypeOptions(opts -> {})
                // Referrer-Policy — 外部サイト遷移時に URL の path/query/hash を漏らさない
                .referrerPolicy(rp -> rp.policy(
                        org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter
                                .ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN
                ))
                // Permissions-Policy — 利用しない機能を明示的に拒否し、サードパーティ iframe からの濫用も防ぐ
                // ※ Spring Security 6.5 の permissionsPolicyHeader DSL がヘッダ送出を行わない既知挙動を
                //   回避するため StaticHeadersWriter で直接書き出している。
                .addHeaderWriter(new StaticHeadersWriter(
                        "Permissions-Policy",
                        "accelerometer=(), camera=(), geolocation=(), gyroscope=(), magnetometer=(), " +
                        "microphone=(), payment=(), usb=()"
                ))
            )
            .formLogin(login -> login
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/slides", true)
                .failureUrl("/admin/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout")
                .permitAll()
            );
        return http.build();
    }
}
