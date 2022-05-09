package com.silenteight.sep.usermanagement.api.identityprovider;

import com.silenteight.sep.usermanagement.api.identityprovider.dto.CreateRoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;

import java.util.List;
import java.util.UUID;

public interface IdentityProviderRoleMapper {

  List<RoleMappingDto> listDefaultIdpMappings();

  List<RoleMappingDto> listMappings(String idProviderName);

  RoleMappingDto addMapping(CreateRoleMappingDto roleMappingDto);

  void deleteMapping(UUID roleMappingId);

  RoleMappingDto getMapping(UUID roleMappingId);
}
