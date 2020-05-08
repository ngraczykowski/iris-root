package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

@Slf4j
public class RegisterExternalUserUseCase extends BaseRegisterUserUseCase {

  @NonNull
  private final AuditTracer auditTracer;

  public RegisterExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer) {

    super(userRegisteringDomainService, registeredUserRepository);
    this.auditTracer = auditTracer;
  }

  public Either<UserDomainError, Success> apply(RegisterExternalUserCommand command) {
    log.info(USER_MANAGEMENT, "Registering external user. command={}", command);

    auditTracer.save(
        new ExternalUserCreationRequestedEvent(
            command.getUsername(), RegisterExternalUserCommand.class.getName(), command));

    return register(command.toUserRegistration());
  }

  @Value
  @Builder
  public static class RegisterExternalUserCommand {

    @NonNull
    private final String username;
    @Nullable
    private final String displayName;
    @NonNull
    @Builder.Default
    private final Set<String> roles = emptySet();
    @NonNull
    private final String origin;

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, empty(), roles),
          origin);
    }
  }
}
