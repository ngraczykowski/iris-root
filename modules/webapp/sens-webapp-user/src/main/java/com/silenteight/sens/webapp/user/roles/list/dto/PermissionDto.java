package com.silenteight.sens.webapp.user.roles.list.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class PermissionDto {

  @NonNull
  UUID id;

  @NonNull
  String group;

  @NonNull
  String name;

  @NonNull
  String description;
}
