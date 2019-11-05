package com.silenteight.sens.webapp.security.acl.dto;

import lombok.NonNull;
import lombok.Value;

@Value
public class PermissionQuery {

  long userId;

  @NonNull
  String objectType;
}
