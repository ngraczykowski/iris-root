package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sep.usermanagement.api.error.UserDomainError;
import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.UserCreator;
import com.silenteight.sep.usermanagement.api.user.dto.NewUserDetails;
import com.silenteight.sep.usermanagement.api.user.dto.NewUserDetails.Credentials;

import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.audit.api.trace.AuditEventUtils.OBFUSCATED_STRING;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static java.util.Collections.emptySet;

@Slf4j
public class RegisterInternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      UserCreator userCreator,
      AuditTracer auditTracer,
      String rolesScope,
      String countryGroupsScope) {

    super(
        userRegisteringDomainService,
        userCreator,
        auditTracer,
        rolesScope,
        countryGroupsScope);
  }

  public Either<UserDomainError, Success> apply(RegisterInternalUserCommand command) {
    log.info(USER_MANAGEMENT, "Registering internal user. command={}", command);

    auditTracer.save(
        new InternalUserCreationRequestedEvent(
            command.getUsername(), RegisterInternalUserCommand.class.getName(),
            command.toEventCommand()));

    return register(command.toUserRegistration(rolesScope, countryGroupsScope));
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
    @NonNull
    @Builder.Default
    private final Set<String> countryGroups = emptySet();

    NewUserRegistration toUserRegistration(String rolesClientId, String countryGroupsClientId) {
      return new NewUserRegistration(
          new NewUserDetails(
              username,
              displayName,
              new Credentials(password),
              scopeRoles(rolesClientId, countryGroupsClientId)),
          SENS_ORIGIN);
    }

    RegisterInternalUserCommand toEventCommand() {
      return new RegisterInternalUserCommand(
          username, OBFUSCATED_STRING, displayName, roles, countryGroups);
    }

    private UserRoles scopeRoles(
        String rolesClientId, String countryGroupsClientId) {

      return new ScopeUserRoles(
          Map.of(
              rolesClientId, new ArrayList<>(roles),
              countryGroupsClientId, new ArrayList<>(countryGroups)));
    }
  }
}
