package com.silenteight.sens.webapp.backend.user.registration.domain;

import lombok.RequiredArgsConstructor;

import io.vavr.control.Either;

@RequiredArgsConstructor
public class UserRegisteringDomainService {

  private final InternalUserRegisterer internalUserRegisterer;
  private final ExternalUserRegisterer externalUserRegisterer;

  public Either<UserRegistrationDomainError, CompletedUserRegistration> register(
      NewUserRegistration newUserRegistration) {

    return newUserRegistration.isInternal()
           ? internalUserRegisterer.register(newUserRegistration)
           : externalUserRegisterer.register(newUserRegistration);
  }
}
