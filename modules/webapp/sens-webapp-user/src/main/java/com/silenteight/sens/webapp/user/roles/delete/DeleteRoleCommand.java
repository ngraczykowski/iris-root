package com.silenteight.sens.webapp.user.roles.delete;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
class DeleteRoleCommand {

  @NonNull
  UUID id;

  @NonNull
  String deletedBy;
}
