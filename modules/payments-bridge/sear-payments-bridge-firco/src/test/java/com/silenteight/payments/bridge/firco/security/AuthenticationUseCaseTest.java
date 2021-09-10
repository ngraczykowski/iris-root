package com.silenteight.payments.bridge.firco.security;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseTest {

  /*
  private AuthenticateUseCase authenticateUseCase;
  @Mock
  private AuthenticationProvider authenticationProvider;

  @BeforeEach
  void setUp() {
    authenticateUseCase = new AuthenticateUseCase(new ProviderManager(authenticationProvider));
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

   */
}
