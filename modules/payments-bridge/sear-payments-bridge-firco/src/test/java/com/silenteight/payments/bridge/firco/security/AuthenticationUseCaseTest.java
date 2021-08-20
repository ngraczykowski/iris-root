package com.silenteight.payments.bridge.firco.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseTest {

  private AuthenticateUseCase authenticateUseCase;
  @Mock
  private SecurityContextUseCase securityContextUseCase;

  @BeforeEach
  void setUp() {
    authenticateUseCase =
        new AuthenticateUseCase(new InMemorySecurityDataAccess(), securityContextUseCase);
  }

  @Test
  void shouldAuthenticate() {
    var user = new UsernamePasswordAuthenticationToken("username", "password");
    authenticateUseCase.authenticate(user);
    verify(securityContextUseCase).setAuthentication(any(), any());
  }

  @Test
  void shouldNotAuthenticate() {
    var user = new UsernamePasswordAuthenticationToken("usernamasde", "passadsword");
    Assertions.assertThrows(
        AccessDeniedException.class, () -> authenticateUseCase.authenticate(user));
  }
}
