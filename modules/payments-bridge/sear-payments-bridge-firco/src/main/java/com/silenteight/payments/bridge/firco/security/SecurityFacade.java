package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityFacade {

  private final AuthenticateUseCase authenticateUseCase;

  public void authenticate(String realm, Authentication authentication) {
    authenticateUseCase.authenticate(realm, authentication);
  }
}
