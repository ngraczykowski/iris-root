package com.silenteight.sens.webapp.user.roles.edit;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
class EditRoleCommand {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  String description;

  @NonNull
  String updatedBy;
}
