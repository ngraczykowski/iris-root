package com.silenteight.sens.webapp.user.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.registration.CreatedUserDetails.ObfuscatedCredentials;
import com.silenteight.sens.webapp.user.registration.CreatedUserDetails.UserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sep.usermanagement.api.CompletedUserRegistration;
import com.silenteight.sep.usermanagement.api.RegisteredUserRepository;
import com.silenteight.sep.usermanagement.api.UserDomainError;

import io.vavr.control.Either;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@RequiredArgsConstructor
@Slf4j
abstract class BaseRegisterUserUseCase {

  @NonNull
  private final UserRegisteringDomainService userRegisteringDomainService;
  @NonNull
  private final RegisteredUserRepository registeredUserRepository;
  @NonNull
  protected final AuditTracer auditTracer;
  @NonNull
  protected final String rolesScope;
  @NonNull
  protected final String countryGroupsScope;

  Either<UserDomainError, Success> register(NewUserRegistration registration) {
    Either<UserDomainError, CompletedUserRegistration> result =
        userRegisteringDomainService.register(registration);

    result.forEach(userRegistration -> {
      auditTracer.save(
          new UserCreatedEvent(
              userRegistration.getUsername(),
              CompletedUserRegistration.class.getName(),
              toCreatedUserDetails(userRegistration)));

      registeredUserRepository.save(userRegistration);

      auditTracer.save(
          new RolesAssignedEvent(
              userRegistration.getUsername(),
              CompletedUserRegistration.class.getName(),
              userRegistration.getRoles()));
    });
    log.info(USER_MANAGEMENT, "User registration result={}", result);

    return result.map(completedRegistration -> completedRegistration::getUsername);
  }

  private CreatedUserDetails toCreatedUserDetails(CompletedUserRegistration registration) {
    return new CreatedUserDetails(
        new UserDetails(
            registration.getUsername(),
            registration.getDisplayName(),
            new ObfuscatedCredentials(),
            registration.getRoles().getRoles(rolesScope)),
        registration.getOrigin(),
        registration.getRegistrationDate());
  }

  public interface Success {

    String getUsername();
  }
}
