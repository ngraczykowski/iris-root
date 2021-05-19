package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientMappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  public Map<String, List<String>> getForUserId(String userId, Set<String> roleClientIds) {
    Map<String, ClientMappingsRepresentation> rolesMappings = getClientRolesMappings(userId);
    Map<String, List<String>> result = new HashMap<>();
    for (String roleClientId: roleClientIds)
      result.put(roleClientId, fetchUserRoles(rolesMappings, roleClientId));

    return result;
  }

  private List<String> fetchUserRoles(
      Map<String, ClientMappingsRepresentation> rolesMappings, String roleClientId) {

    return ofNullable(rolesMappings.get(roleClientId))
        .map(ClientMappingsRepresentation::getMappings)
        .orElse(emptyList())
        .stream()
        .map(RoleRepresentation::getName)
        .filter(internalRoleFilter)
        .sorted()
        .collect(toList());
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
