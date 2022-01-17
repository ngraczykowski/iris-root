package com.silenteight.sens.webapp.role.create.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDto {

  @NonNull
  UUID id;
  @NonNull
  String name;
  @NonNull
  String description;
  @NonNull
  Set<UUID> permissions;
}
