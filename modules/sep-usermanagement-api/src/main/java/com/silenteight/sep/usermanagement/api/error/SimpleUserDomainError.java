package com.silenteight.sep.usermanagement.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SimpleUserDomainError implements UserDomainError {

  private static final long serialVersionUID = -8726530296633986536L;

  private final String reason;
}
