package com.silenteight.serp.governance.policy.create;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
class CreatePolicyCommand {

  @NonNull
  private final UUID id;
  @NonNull
  private final String policyName;
  @NonNull
  private final String createdBy;
}
