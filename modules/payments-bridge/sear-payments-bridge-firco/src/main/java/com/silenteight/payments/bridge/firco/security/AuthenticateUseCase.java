package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class AuthenticateUseCase {

  void authenticate(String realm, Authentication authentication) {
    // TODO(ahaczewski): Implement authenticate.
  }
}
