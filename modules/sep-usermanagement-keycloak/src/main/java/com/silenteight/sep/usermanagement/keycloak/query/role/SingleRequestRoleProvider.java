package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SingleRequestRoleProvider implements RolesProvider {

  @NonNull
  private final RealmResource realmResource;
  @NonNull
  private final InternalRoleFilter internalRoleFilter;

  @Override
  public List<String> getForUserId(String userId) {
    List<String> r = ofNullable(fetchUserRoles(userId))
        .orElse(emptyList())
        .stream()
        .map(RoleRepresentation::getName)
        .sorted().collect(toList());
    return r.stream()
        .filter(internalRoleFilter)
        .collect(toList());
  }

  private List<RoleRepresentation> fetchUserRoles(String userId) {
    return realmResource
        .users()
        .get(userId)
        .roles()
        .getAll()
        .getRealmMappings();
  }
}
