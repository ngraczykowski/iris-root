package com.silenteight.sens.webapp.sso.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AttributeToRoleDto {

  @NonNull
  String attribute;

  @NonNull
  String role;
}
