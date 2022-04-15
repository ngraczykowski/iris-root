package com.silenteight.sens.webapp.sso.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.sso.details.SsoMappingDetailsQuery;
import com.silenteight.sens.webapp.sso.list.ListSsoMappingsQuery;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderRoleMapper;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class SsoMappingsDetailsQuery implements ListSsoMappingsQuery, SsoMappingDetailsQuery {

  @NonNull
  private final IdentityProviderRoleMapper identityProviderRoleMapper;

  @NonNull
  private final RoleMappingDtoToSsoMappingDtoConverter roleMappingDtoToSsoMappingDtoConverter;

  @Override
  public Collection<SsoMappingDto> listAll() {
    return identityProviderRoleMapper.listDefaultIdpMappings()
        .stream()
        .map(roleMappingDtoToSsoMappingDtoConverter::convert)
        .collect(toList());
  }

  @Override
  public SsoMappingDto details(UUID ssoMappingId) {
    return roleMappingDtoToSsoMappingDtoConverter.convert(
        identityProviderRoleMapper.getMapping(ssoMappingId));
  }
}
