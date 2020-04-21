package com.silenteight.sens.webapp.keycloak.usermanagement.registration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserId;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;


@RequiredArgsConstructor
class KeycloakUserCreator {

  private final UsersResource usersResource;
  private final AuditLog auditLog;

  KeycloakUserId create(UserRepresentation userRepresentation) {
    auditLog.logInfo(
        USER_MANAGEMENT, "Creating new User. username={}", userRepresentation.getUsername());

    try (Response response = usersResource.create(userRepresentation)) {
      boolean isSuccessful = response.getStatusInfo().getFamily() == Family.SUCCESSFUL;

      if (!isSuccessful)
        throw new CreateUserException(response);

      return getUserId(response);
    }
  }

  private static KeycloakUserId getUserId(Response response) {
    URI location = response.getLocation();

    return KeycloakUserId.of(getLastSegment(location));
  }

  private static String getLastSegment(URI location) {
    String path = location.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }
}
