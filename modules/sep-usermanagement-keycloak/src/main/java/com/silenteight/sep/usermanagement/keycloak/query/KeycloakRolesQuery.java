package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.RolesQuery;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.keycloak.query.role.InternalRoleFilter;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakRolesQuery implements RolesQuery {

  @NonNull
  private final RolesResource rolesResource;
  @NonNull
  private final InternalRoleFilter internalRoleFilter;

  @Override
  public RolesDto list() {
    return new RolesDto(fetchRoleNames());
  }

  private List<String> fetchRoleNames() {
    return rolesResource
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .sorted()
        .filter(internalRoleFilter)
        .collect(toList());
  }
}