package com.silenteight.payments.bridge.firco.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.assertj.core.api.Assertions.*;

class AuthenticateServiceTest {

  private TestingAuthenticationToken testingToken;
  private AuthenticateService service;

  @BeforeEach
  void setUp() {
    testingToken = new TestingAuthenticationToken("testing user", "password");

    var providerManager = new ProviderManager(new TestingAuthenticationProvider());
    providerManager.afterPropertiesSet();

    service = new AuthenticateService(providerManager);
    AuthenticateService.setAuthentication(null);
  }

  @Test
  void doesNothingWhenAuthenticated() {
    AuthenticateService.setAuthentication(testingToken);

    var otherToken = new TestingAuthenticationToken("other user", "other password");
    assertThat(service.authenticate(otherToken)).isFalse();
  }

  @Test
  void authenticates() {
    assertThat(service.authenticate(testingToken)).isTrue();
  }

  @Test
  void throwsWhenFailsToAuthenticate() {
    service = new AuthenticateService(authentication -> {
      throw new DisabledException("testing");
    });

    assertThatThrownBy(() -> service.authenticate(testingToken)).isInstanceOf(
        DisabledException.class);
  }
}
