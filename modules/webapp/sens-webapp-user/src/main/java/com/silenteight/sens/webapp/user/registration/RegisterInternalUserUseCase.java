package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.audit.trace.AuditEventUtils.OBFUSCATED_STRING;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.SensOrigin.SENS_ORIGIN;
import static java.util.Collections.emptySet;
import static java.util.Optional.of;

@Slf4j
public class RegisterInternalUserUseCase extends BaseRegisterUserUseCase {

  @NonNull
  private final AuditTracer auditTracer;

  public RegisterInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditTracer auditTracer) {

    super(userRegisteringDomainService, registeredUserRepository);
    this.auditTracer = auditTracer;
  }

  public Either<UserDomainError, Success> apply(RegisterInternalUserCommand command) {
    log.info(USER_MANAGEMENT, "Registering internal user. command={}", command);

    auditTracer.save(
        new InternalUserCreationRequestedEvent(
            command.getUsername(), RegisterInternalUserCommand.class.getName(),
            command.toEventCommand()));

    return register(command.toUserRegistration());
  }

  @Value
  @Builder
  public static class RegisterInternalUserCommand {

    @NonNull
    private final String username;
    @NonNull
    @ToString.Exclude
    private final String password;
    @Nullable
    private final String displayName;
    @NonNull
    @Builder.Default
    private final Set<String> roles = emptySet();

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, of(new Credentials(password)), roles),
          SENS_ORIGIN);
    }

    RegisterInternalUserCommand toEventCommand() {
      return new RegisterInternalUserCommand(username, OBFUSCATED_STRING, displayName, roles);
    }
  }
}
