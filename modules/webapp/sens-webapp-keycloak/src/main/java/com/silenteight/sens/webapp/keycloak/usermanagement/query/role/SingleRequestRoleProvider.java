package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SingleRequestRoleProvider implements RolesProvider {

  private final RealmResource realmResource;

  @Override
  public List<String> getForUserId(String userId) {
    return realmResource
        .users()
        .get(userId)
        .roles()
        .getAll()
        .getRealmMappings()
        .stream()
        .map(RoleRepresentation::getName)
        .collect(toList());
  }
}
