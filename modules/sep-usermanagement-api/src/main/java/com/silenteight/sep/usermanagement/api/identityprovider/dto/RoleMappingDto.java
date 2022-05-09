package com.silenteight.sep.usermanagement.api.identityprovider.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class RoleMappingDto {

  private UUID id;
  private String providerAlias;

  @NonNull
  private String name;
  @Builder.Default
  private Set<SsoAttributeDto> ssoAttributes = new HashSet<>();
  @Builder.Default
  private RolesDto rolesDto = new RolesDto(new ArrayList<>());

  public void addTargetRole(String role) {
    rolesDto.getRoles().add(role);
  }

  public void addSsoAttributes(Set<SsoAttributeDto> attrs) {
    ssoAttributes.addAll(attrs);
  }
}
