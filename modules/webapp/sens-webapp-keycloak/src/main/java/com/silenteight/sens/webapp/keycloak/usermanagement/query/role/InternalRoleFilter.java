package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.function.Predicate;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.ROLE_ORIGIN;
import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class InternalRoleFilter implements Predicate<String> {

  private static final String ORIGIN_WEBAPP_VALUE = "webapp";

  @NonNull
  private final RolesResource rolesResource;

  @Override
  public boolean test(String role) {
    return ofNullable(rolesResource.get(role))
        .map(RoleResource::toRepresentation)
        .map(InternalRoleFilter::isWebAppRole)
        .orElse(FALSE);
  }

  private static boolean isWebAppRole(RoleRepresentation roleRepresentation) {
    return ofNullable(roleRepresentation.getAttributes())
        .flatMap(map -> ofNullable(map.get(ROLE_ORIGIN)))
        .filter(val -> val.contains(ORIGIN_WEBAPP_VALUE))
        .isPresent();
  }
}
