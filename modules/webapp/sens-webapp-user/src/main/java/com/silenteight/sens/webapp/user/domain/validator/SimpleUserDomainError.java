package com.silenteight.sens.webapp.user.domain.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class SimpleUserDomainError implements UserDomainError {

  private static final long serialVersionUID = -8726530296633986536L;

  private final String reason;
}
