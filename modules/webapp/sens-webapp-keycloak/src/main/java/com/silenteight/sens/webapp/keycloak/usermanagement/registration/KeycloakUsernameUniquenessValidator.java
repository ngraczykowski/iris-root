package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator;

import io.vavr.control.Option;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@RequiredArgsConstructor
class KeycloakUsernameUniquenessValidator implements UsernameUniquenessValidator {

  private final UsersResource userResource;
  private final AuditLog auditLog;

  @Override
  public Option<UsernameNotUniqueError> validate(String username) {
    auditLog.logInfo(USER_MANAGEMENT, "Checking if username is unique in Keycloak. {}", username);

    if (isUnique(username))
      return none();

    return of(new UsernameNotUniqueError(username));
  }

  private boolean isUnique(String username) {
    return userResource
        .search(username)
        .stream()
        .map(UserRepresentation::getUsername)
        .noneMatch(name -> name.equals(username));
  }
}
