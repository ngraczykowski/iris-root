package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.UserRoles;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientMappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
class SingleRequestRoleProvider implements RolesProvider {

  @NonNull
  private final RealmResource realmResource;

  @Override
  public UserRoles getForUserId(String userId, Set<String> roleClientIds) {
    Map<String, ClientMappingsRepresentation> rolesMappings = getClientRolesMappings(userId);
    return new ExtractedUserRoles(getRolesForScopes(roleClientIds, rolesMappings));
  }

  private Map<String, Set<String>> getRolesForScopes(
      Set<String> roleClientIds, Map<String, ClientMappingsRepresentation> rolesMappings) {

    Map<String, Set<String>> result = new HashMap<>();
    for (String roleClientId: roleClientIds)
      result.put(roleClientId, fetchUserRoles(rolesMappings, roleClientId));

    return result;
  }

  private Set<String> fetchUserRoles(
      Map<String, ClientMappingsRepresentation> rolesMappings, String roleClientId) {

    return ofNullable(rolesMappings.get(roleClientId))
        .map(ClientMappingsRepresentation::getMappings)
        .orElse(emptyList())
        .stream()
        .map(RoleRepresentation::getName)
        .collect(toSet());
  }

  private Map<String, ClientMappingsRepresentation> getClientRolesMappings(String userId) {
    return realmResource
        .users()
        .get(userId)
        .roles()
        .getAll()
        .getClientMappings();
  }
}
