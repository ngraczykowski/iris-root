package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
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


import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.Collections.emptySet;

@Slf4j
public class RegisterExternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterExternalUserUseCase(
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

  public Either<UserDomainError, Success> apply(RegisterExternalUserCommand command) {
    log.info(USER_MANAGEMENT, "Registering external user. command={}", command);

    auditTracer.save(
        new ExternalUserCreationRequestedEvent(
            command.getUsername(), RegisterExternalUserCommand.class.getName(), command));

    return register(command.toUserRegistration(rolesScope, countryGroupsScope));
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
    @Builder.Default
    private final Set<String> countryGroups = emptySet();
    @NonNull
    private final String origin;

    NewUserRegistration toUserRegistration(String rolesClientId, String countryGroupsClientId) {
      return new NewUserRegistration(
          new NewUserDetails(
              username,
              displayName,
              scopeRoles(rolesClientId, countryGroupsClientId)),
          origin);
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
