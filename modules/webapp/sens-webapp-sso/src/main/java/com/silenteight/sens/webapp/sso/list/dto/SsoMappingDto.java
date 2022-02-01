package com.silenteight.sens.webapp.sso.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Value
public class SsoMappingDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  Set<AttributeToRoleDto> attributeToRoleDtoSet;

  @NonNull
  List<String> roles;
}
