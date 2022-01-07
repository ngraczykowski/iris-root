package com.silenteight.sens.webapp.user.roles.list.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.user.roles.domain.RoleState;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class RoleDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  String description;

  @NonNull
  RoleState state;

  @NonNull
  OffsetDateTime updatedAt;

  @NonNull
  OffsetDateTime createdAt;

  @NonNull
  String createdBy;
}
