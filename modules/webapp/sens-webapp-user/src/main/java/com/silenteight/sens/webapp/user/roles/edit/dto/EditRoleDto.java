package com.silenteight.sens.webapp.user.roles.edit.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRoleDto {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  String description;
}
