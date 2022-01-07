package com.silenteight.sens.webapp.user.roles.create;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
class CreateRoleCommand {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  String description;

  @NonNull
  String createdBy;
}
