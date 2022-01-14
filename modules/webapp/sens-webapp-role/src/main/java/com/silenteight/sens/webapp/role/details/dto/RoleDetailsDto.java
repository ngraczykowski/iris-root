package com.silenteight.sens.webapp.role.details.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class RoleDetailsDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String description;
}
