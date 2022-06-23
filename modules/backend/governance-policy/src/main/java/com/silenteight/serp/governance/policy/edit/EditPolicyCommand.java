package com.silenteight.serp.governance.policy.edit;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Builder
@Data
class EditPolicyCommand {

  @NonNull
  private final UUID id;
  private final String policyName;
  private final String description;
  @NonNull
  private final String updatedBy;
}
