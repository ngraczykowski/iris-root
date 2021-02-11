package com.silenteight.serp.governance.policy.step.delete;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
class DeleteStepCommand {

  @NonNull
  UUID id;
  @NonNull
  String updatedBy;
}
