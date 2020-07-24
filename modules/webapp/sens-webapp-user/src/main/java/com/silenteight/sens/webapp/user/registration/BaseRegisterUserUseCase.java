package com.silenteight.sens.webapp.user.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@RequiredArgsConstructor
@Slf4j
abstract class BaseRegisterUserUseCase {

  @NonNull
  private final UserRegisteringDomainService userRegisteringDomainService;
  @NonNull
  private final RegisteredUserRepository registeredUserRepository;

  Either<UserDomainError, Success> register(NewUserRegistration registration) {
    Either<UserDomainError, CompletedUserRegistration> result =
        userRegisteringDomainService.register(registration);

    result.forEach(registeredUserRepository::save);
    log.info(USER_MANAGEMENT, "User registration result={}", result);

    return result.map(completedRegistration -> completedRegistration::getUsername);
  }

  public interface Success {

    String getUsername();
  }
}
