package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderQuery;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.IdentityProviderDto;

import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import java.util.Collection;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class KeycloakIdentityProviderQuery implements IdentityProviderQuery {

  @NonNull
  private final IdentityProvidersResource identityProvidersResource;

  @Override
  public Collection<IdentityProviderDto> listAll() {
    return identityProvidersResource.findAll().stream()
        .map(KeycloakIdentityProviderQuery::toIdentityProviderDto)
        .collect(toList());
  }

  private static IdentityProviderDto toIdentityProviderDto(
      IdentityProviderRepresentation identityProviderRepresentation) {

    String displayName = ofNullable(identityProviderRepresentation.getDisplayName())
        .orElse(identityProviderRepresentation.getAlias());

    return IdentityProviderDto.builder()
        .alias(identityProviderRepresentation.getAlias())
        .displayName(displayName)
        .internalId(identityProviderRepresentation.getInternalId())
        .enabled(identityProviderRepresentation.isEnabled())
        .build();
  }
}
