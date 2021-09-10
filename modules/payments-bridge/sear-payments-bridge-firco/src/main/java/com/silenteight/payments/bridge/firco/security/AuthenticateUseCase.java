package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class AuthenticateUseCase {

  private final AuthenticationManager authenticationManager;

  void authenticate(Authentication authentication) {
    if (isAuthenticated())
      return;

    var authenticated = authenticationManager.authenticate(authentication);

    setAuthentication(authenticated);
  }

  private boolean isAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication() != null;
  }

  private void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
