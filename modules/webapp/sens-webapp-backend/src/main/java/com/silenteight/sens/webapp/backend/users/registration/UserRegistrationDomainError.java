package com.silenteight.sens.webapp.backend.users.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class UserRegistrationDomainError implements Serializable {

  private static final long serialVersionUID = -8308734811572088956L;

  private final String reason;
}
