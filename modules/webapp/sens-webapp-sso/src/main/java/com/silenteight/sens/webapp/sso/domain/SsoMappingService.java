package com.silenteight.sens.webapp.sso.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.sso.create.CreateSsoMappingCommand;
import com.silenteight.sens.webapp.sso.delete.DeleteSsoMappingRequest;
import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;
import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderRoleMapper;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.CreateRoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@AllArgsConstructor
public class SsoMappingService {

  @NonNull
  private final IdentityProviderRoleMapper identityProviderRoleMapper;

  public void create(CreateSsoMappingCommand command) {
    CreateRoleMappingDto createRoleMappingDto = CreateRoleMappingDto.builder()
        .name(command.getName())
        .ssoAttributes(getSsoAttributeDtoList(command))
        .roles(createRolesDto(command))
        .build();

    identityProviderRoleMapper.addMapping(createRoleMappingDto);
  }

  public void deleteSsoMapping(DeleteSsoMappingRequest request) {
    identityProviderRoleMapper.deleteMapping(request.getSsoMappingId());
  }

  private static RolesDto createRolesDto(CreateSsoMappingCommand command) {
    return new RolesDto(command.getRoles());
  }

  private static SsoAttributeDto createSsoAttributeDto(AttributeToRoleDto attributeToRoleDto) {
    return SsoAttributeDto.builder()
        .key(attributeToRoleDto.getAttribute())
        .value(attributeToRoleDto.getRole())
        .build();
  }

  private static Set<SsoAttributeDto> getSsoAttributeDtoList(CreateSsoMappingCommand command) {
    return command.getAttributes().stream()
        .map(SsoMappingService::createSsoAttributeDto)
        .collect(toSet());
  }
}
