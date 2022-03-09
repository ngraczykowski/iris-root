package com.silenteight.sens.webapp.sso.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;

@Builder
@Value
public class SsoMappingDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  @Valid
  Set<AttributeToRoleDto> attributes;

  @NonNull
  List<String> roles;
}
