package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.role.RolesQuery;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakRolesQuery implements RolesQuery {

  @NonNull
  private final ClientQuery clientQuery;
  @NonNull
  private final ClientsResource clientsResource;

  @Override
  public RolesDto list(String scope) {
    return new RolesDto(fetchRoleNames(scope));
  }

  private List<String> fetchRoleNames(String scope) {
    return clientsResource
        .get(clientQuery.getByClientId(scope).getId())
        .roles()
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .collect(toList());
  }
}
