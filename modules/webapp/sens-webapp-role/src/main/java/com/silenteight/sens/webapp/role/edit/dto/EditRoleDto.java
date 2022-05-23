package com.silenteight.sens.webapp.role.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRoleDto {

  @Nullable
  String name;
  @Nullable
  String description;
  @Nullable
  Set<UUID> permissions;
}
