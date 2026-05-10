package com.portfolio.cms.service;

import com.portfolio.cms.entity.UserAccount;
import com.portfolio.cms.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock UserAccountRepository userRepository;
    @InjectMocks CustomUserDetailsService service;

    @Test
    @DisplayName("ユーザー存在: UserDetails が ROLE_ADMIN 付きで返る")
    void loadUserByUsername_existingUser_returnsUserDetailsWithAdminRole() {
        UserAccount user = UserAccount.builder()
                .username("admin")
                .passwordHash("$2a$10$hashedpassword")
                .build();
        given(userRepository.findByUsername("admin")).willReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("admin");

        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getPassword()).isEqualTo("$2a$10$hashedpassword");
        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("ユーザー不在: UsernameNotFoundException がスローされる")
    void loadUserByUsername_unknownUser_throwsUsernameNotFoundException() {
        given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("ghost");
    }
}
