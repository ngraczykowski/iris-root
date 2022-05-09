package com.silenteight.sep.usermanagement.api.identityprovider.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

@Data
@Builder
public class CreateRoleMappingDto {

  @NonNull
  private String name;
  @Builder.Default
  private Set<SsoAttributeDto> ssoAttributes = emptySet();
  @Builder.Default
  private RolesDto roles = new RolesDto(emptyList());
}
