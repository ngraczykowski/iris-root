package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.UserOrigin;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

public class RegisterExternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditLog auditLog) {

    super(userRegisteringDomainService, registeredUserRepository, auditLog);
  }

  public Either<UserDomainError, Success> apply(RegisterExternalUserCommand command) {
    auditLog.logInfo(USER_MANAGEMENT, "Registering external user. command={}", command);

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
    private final UserOrigin origin;

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, empty(), roles),
          origin);
    }
  }
}
