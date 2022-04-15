package com.silenteight.sens.webapp.role.details.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class RoleDetailsDto {

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
