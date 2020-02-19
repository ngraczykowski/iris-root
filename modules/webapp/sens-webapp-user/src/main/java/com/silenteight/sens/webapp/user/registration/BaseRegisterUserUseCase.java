package com.silenteight.sens.webapp.user.registration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

import io.vavr.control.Either;

@RequiredArgsConstructor
class BaseRegisterUserUseCase {

  private final UserRegisteringDomainService userRegisteringDomainService;
  private final RegisteredUserRepository registeredUserRepository;

  Either<UserRegistrationDomainError, Success> register(NewUserRegistration registration) {
    Either<UserRegistrationDomainError, CompletedUserRegistration> result =
        userRegisteringDomainService.register(registration);

    result.forEach(registeredUserRepository::save);

    return result.map(completedRegistration -> completedRegistration::getUsername);
  }

  public interface Success {

    String getUsername();
  }
}
