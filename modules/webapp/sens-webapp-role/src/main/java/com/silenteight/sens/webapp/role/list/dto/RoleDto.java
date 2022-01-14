package com.silenteight.sens.webapp.role.list.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Set;
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
  Set<UUID> permissions;
  @NonNull
  OffsetDateTime updatedAt;
  @NonNull
  OffsetDateTime createdAt;
  @NonNull
  String createdBy;
  @NonNull
  String updatedBy;
}