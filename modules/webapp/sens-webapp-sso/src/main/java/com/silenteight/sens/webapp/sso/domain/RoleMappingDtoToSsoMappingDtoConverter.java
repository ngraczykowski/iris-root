package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

class RoleMappingDtoToSsoMappingDtoConverter implements Converter<RoleMappingDto, SsoMappingDto> {

  @Override
  public SsoMappingDto convert(RoleMappingDto roleMappingDto) {
    return SsoMappingDto.builder()
        .id(roleMappingDto.getId())
        .name(roleMappingDto.getName())
        .attributes(toAttributeToRoleDtoSet(roleMappingDto.getSsoAttributes()))
        .roles(getRolesAsString(roleMappingDto.getRolesDto()))
        .build();
  }

  private static List<String> getRolesAsString(RolesDto rolesDto) {
    return rolesDto.getRoles();
  }

  private static Set<AttributeToRoleDto> toAttributeToRoleDtoSet(
      Set<SsoAttributeDto> ssoAttributes) {

    return ssoAttributes.stream()
        .map(RoleMappingDtoToSsoMappingDtoConverter::toAttributeToRoleDto)
        .collect(toSet());
  }

  private static AttributeToRoleDto toAttributeToRoleDto(SsoAttributeDto ssoAttributeDto) {
    return AttributeToRoleDto.builder()
        .attribute(ssoAttributeDto.getKey())
        .role(ssoAttributeDto.getValue())
        .build();
  }
}
