package com.silenteight.sens.webapp.user.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;


@RequiredArgsConstructor
@Slf4j
class BaseRegisterUserUseCase {

  private final UserRegisteringDomainService userRegisteringDomainService;
  private final RegisteredUserRepository registeredUserRepository;
  protected final AuditLog auditLog;

  Either<UserDomainError, Success> register(NewUserRegistration registration) {
    Either<UserDomainError, CompletedUserRegistration> result =
        userRegisteringDomainService.register(registration);

    result.forEach(registeredUserRepository::save);
    auditLog.logInfo(USER_MANAGEMENT, "User registration result={}", result);

    return result.map(completedRegistration -> completedRegistration::getUsername);
  }

  public interface Success {

    String getUsername();
  }
}
