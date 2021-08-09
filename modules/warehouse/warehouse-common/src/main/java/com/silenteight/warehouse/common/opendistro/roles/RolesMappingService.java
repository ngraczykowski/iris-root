package com.silenteight.warehouse.common.opendistro.roles;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.RoleMappingDto;

import java.util.UUID;

import static java.util.Set.of;

@RequiredArgsConstructor
public class RolesMappingService {

  private final OpendistroElasticClient opendistroElasticClient;

  public void attachBackendRoleToRole(UUID role) {
    RoleMappingDto roleMappingDto = RoleMappingDto.builder()
        .backendRoles(of(role.toString()))
        .build();

    opendistroElasticClient.setRoleMapping(role.toString(), roleMappingDto);
  }

  public void removeRoleMapping(UUID id) {
    opendistroElasticClient.removeRoleMapping(id.toString());
  }
}
