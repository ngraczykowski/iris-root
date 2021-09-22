package com.silenteight.payments.bridge.firco.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.security.port.AuthenticateUseCase;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class AuthenticateService implements AuthenticateUseCase {

  private final AuthenticationManager authenticationManager;

  @Override
  public boolean authenticate(Authentication authentication) {
    if (isAuthenticated()) {
      log.debug("Already authenticated!");
      return false;
    }

    var authenticated = authenticationManager.authenticate(authentication);

    setAuthentication(authenticated);

    log.debug("Authenticated successfully!");
    return true;
  }

  static boolean isAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication() != null;
  }

  static void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
