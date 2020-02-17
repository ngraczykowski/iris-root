package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.RolesQuery;
import com.silenteight.sens.webapp.user.dto.RolesDto;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakRolesQuery implements RolesQuery {

  @NonNull
  private final RolesResource rolesResource;

  @Override
  public RolesDto list() {
    return new RolesDto(rolesResource
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .sorted()
        .collect(toList()));
  }
}
