package com.silenteight.sens.webapp.user.roles.create.dto;

import lombok.*;

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
}
