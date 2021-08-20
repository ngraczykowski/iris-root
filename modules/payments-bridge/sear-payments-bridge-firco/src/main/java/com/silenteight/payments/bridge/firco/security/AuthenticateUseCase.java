package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class AuthenticateUseCase {

  private final SecurityDataAccess securityDataAccess;
  private final SecurityContextUseCase securityContextUseCase;

  void authenticate(Authentication authentication) {
    var username = authentication.getPrincipal().toString();
    var password = authentication.getCredentials().toString();

    var isPresent =
        securityDataAccess.findByCredentials(username, password);

    if (!isPresent)
      throw new AccessDeniedException("Couldn't authorize with provided credentials");

    securityContextUseCase.setAuthentication(password, username);
  }
}
