package com.silenteight.sens.webapp.permission.list.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PermissionDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String description;
}
