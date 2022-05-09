package com.silenteight.sep.usermanagement.keycloak.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.Set;

@RequiredArgsConstructor
public class ClientRoleManager {

  @NonNull
  private final ClientsResource clientsResource;

  public void createRoles(String clientId, Set<String> roles) {
    RolesResource rolesResource = clientsResource.get(clientId).roles();
    roles
        .stream()
        .map(this::toRoleRepresentation)
        .forEach(rolesResource::create);
  }

  private RoleRepresentation toRoleRepresentation(String role) {
    RoleRepresentation roleRepresentation = new RoleRepresentation();
    roleRepresentation.setName(role);
    roleRepresentation.setClientRole(true);
    return roleRepresentation;
  }
}
