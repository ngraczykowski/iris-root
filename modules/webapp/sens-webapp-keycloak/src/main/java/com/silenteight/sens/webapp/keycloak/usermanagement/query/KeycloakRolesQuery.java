package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.RolesQuery;
import com.silenteight.sens.webapp.user.dto.RolesDto;

import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakRolesQuery implements RolesQuery {

  private static final String ORIGIN_ATTRIBUTE = "origin";
  private static final String ORIGIN_WEBAPP_VALUE = "webapp";

  @NonNull
  private final RolesResource rolesResource;

  @Override
  public RolesDto list() {
    return new RolesDto(
        rolesResource.list()
            .stream()
            .map(it -> rolesResource.get(it.getName()))
            .map(RoleResource::toRepresentation)
            .filter(KeycloakRolesQuery::isWebAppRole)
            .map(RoleRepresentation::getName)
            .sorted()
            .collect(toList())
    );
  }

  private static boolean isWebAppRole(RoleRepresentation roleRepresentation) {
    return ofNullable(roleRepresentation.getAttributes())
        .flatMap(map -> ofNullable(map.get(ORIGIN_ATTRIBUTE)))
        .filter(val -> val.contains(ORIGIN_WEBAPP_VALUE))
        .isPresent();
  }
}
