package com.silenteight.serp.governance.policy.create;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Data
class CreatePolicyCommand {

  @NonNull
  private final UUID id;
  @NonNull
  private final String name;
  @NonNull
  private final String createdBy;
}
