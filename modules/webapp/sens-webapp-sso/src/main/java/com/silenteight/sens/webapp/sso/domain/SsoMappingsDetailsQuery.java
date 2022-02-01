package com.silenteight.sens.webapp.sso.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.sso.details.SsoMappingDetailsQuery;
import com.silenteight.sens.webapp.sso.list.ListSsoMappingsQuery;
import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;
import com.silenteight.sep.usermanagement.api.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.api.dto.SsoAttributeDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
class SsoMappingsDetailsQuery implements ListSsoMappingsQuery, SsoMappingDetailsQuery {

  @NonNull
  private final IdentityProviderRoleMapper identityProviderRoleMapper;

  @Override
  public Collection<SsoMappingDto> listAll() {
    return identityProviderRoleMapper.listDefaultIdpMappings()
        .stream()
        .map(SsoMappingsDetailsQuery::toSsoMappingDto)
        .collect(toList());
  }

  @Override
  public SsoMappingDto details(UUID ssoMappingId) {
    return toSsoMappingDto(identityProviderRoleMapper.getMapping(ssoMappingId));
  }

  private static SsoMappingDto toSsoMappingDto(RoleMappingDto roleMappingDto) {
    return SsoMappingDto.builder()
        .id(roleMappingDto.getId())
        .name(roleMappingDto.getName())
        .attributeToRoleDtoSet(toAttributeToRoleDtoSet(roleMappingDto.getSsoAttributes()))
        .roles(getRolesAsString(roleMappingDto.getRolesDto()))
        .build();
  }

  private static List<String> getRolesAsString(RolesDto rolesDto) {
    return rolesDto.getRoles();
  }

  private static Set<AttributeToRoleDto> toAttributeToRoleDtoSet(
      Set<SsoAttributeDto> ssoAttributes) {

    return ssoAttributes.stream()
        .map(SsoMappingsDetailsQuery::toAttributeToRoleDto)
        .collect(toSet());
  }

  private static AttributeToRoleDto toAttributeToRoleDto(SsoAttributeDto ssoAttributeDto) {
    return AttributeToRoleDto.builder()
        .key(ssoAttributeDto.getKey())
        .value(ssoAttributeDto.getValue())
        .build();
  }
}
