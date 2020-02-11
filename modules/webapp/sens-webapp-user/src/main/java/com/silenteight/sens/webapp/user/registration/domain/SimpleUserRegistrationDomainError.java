package com.silenteight.sens.webapp.user.registration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class SimpleUserRegistrationDomainError implements UserRegistrationDomainError {

  private static final long serialVersionUID = -8726530296633986536L;

  private final String reason;
}
