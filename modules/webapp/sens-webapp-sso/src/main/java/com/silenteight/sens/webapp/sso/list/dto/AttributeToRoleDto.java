package com.silenteight.sens.webapp.sso.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class AttributeToRoleDto {

  @NonNull
  String key;

  @NonNull
  String value;
}
