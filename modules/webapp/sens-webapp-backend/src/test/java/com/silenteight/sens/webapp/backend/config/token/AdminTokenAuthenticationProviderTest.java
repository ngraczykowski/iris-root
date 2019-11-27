package com.silenteight.sens.webapp.backend.config.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTokenAuthenticationProviderTest {

  private static final String ADMIN_TOKEN = "admin-token";
  private static final String ADMIN_USER_NAME = "admin";

  @Mock
  private UsernamePasswordAuthenticationToken authentication;

  private AdminTokenAuthenticationProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new AdminTokenAuthenticationProvider(new AdminTokenSecurity(ADMIN_TOKEN));
  }

  @Test
  void shouldRetrieveUserForAdminToken() {
    // given
    doReturn(ADMIN_TOKEN).when(authentication).getCredentials();

    // when
    UserDetails userDetails = underTest.retrieveUser(null, authentication);

    // then
    assertThat(userDetails.getUsername()).isEqualTo(ADMIN_USER_NAME);
  }

  @Test
  void shouldThrowUsernameNotFoundExceptionForUnknownToken() {
    // given
    doReturn("unknown-token").when(authentication).getCredentials();

    // when
    assertThrows(
        UsernameNotFoundException.class,
        () -> underTest.retrieveUser(null, authentication)
    );
  }
}
