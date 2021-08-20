package com.silenteight.payments.bridge.firco.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

class SecurityContextUseCaseTest {

  SecurityContextUseCase securityContextUseCase;

  @BeforeEach
  void setUp() {
    securityContextUseCase =
        new SecurityContextUseCase(Collections.singleton(new SimpleGrantedAuthority("asd")));
  }

  @Test
  void shouldAuthenticate() {
    securityContextUseCase.setAuthentication("asd", "asd");
    var isAuth = SecurityContextHolder
        .getContext().getAuthentication().isAuthenticated();
    Assertions.assertTrue(isAuth);
  }
}
