package com.silenteight.sens.webapp.sso.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Builder
@Value
public class SsoMappingDto {

  @NonNull
  String name;

  @NonNull
  Set<AttributeToRoleDto> attributeToRoleDtoSet;

  @NonNull
  List<String> roles;
}
