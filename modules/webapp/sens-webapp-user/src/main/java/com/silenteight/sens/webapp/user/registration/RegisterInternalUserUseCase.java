package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Collections.emptySet;
import static java.util.Optional.of;

public class RegisterInternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository,
      AuditLog auditLog) {

    super(userRegisteringDomainService, registeredUserRepository, auditLog);
  }

  public Either<UserDomainError, Success> apply(RegisterInternalUserCommand command) {
    auditLog.logInfo(USER_MANAGEMENT, "Registering internal user. command={}", command);

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
          SENS);
    }
  }
}
