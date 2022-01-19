package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.IdentityProviderRepository;
import com.silenteight.sep.usermanagement.api.dto.IdentityProviderDto;

import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import java.util.Collection;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class IdentityProviderQuery implements IdentityProviderRepository {

  @NonNull
  private final IdentityProvidersResource identityProvidersResource;

  @Override
  public Collection<IdentityProviderDto> findAll() {
    return identityProvidersResource.findAll().stream()
        .map(IdentityProviderQuery::toIdentityProviderDto)
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
