package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.experimental.UtilityClass;

import org.keycloak.admin.client.resource.IdentityProviderResource;
import org.keycloak.representations.idm.IdentityProviderMapperRepresentation;

import java.util.List;
import java.util.UUID;

import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.extractId;
import static java.util.stream.Collectors.toList;

@UtilityClass
final class KeycloakMapperFinder {

  static List<IdentityProviderMapperRepresentation> findKeycloakMappersBySharedId(
      IdentityProviderResource idpResource, UUID aggregateId) {

    return idpResource
        .getMappers()
        .stream()
        .filter(m -> aggregateId.toString().equals(extractId(m.getName())))
        .collect(toList());
  }
}
