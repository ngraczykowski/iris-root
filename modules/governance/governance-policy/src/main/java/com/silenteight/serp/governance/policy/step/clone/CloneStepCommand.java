package com.silenteight.serp.governance.policy.step.clone;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class CloneStepCommand {

  @NonNull
  UUID newStepId;
  @NonNull
  UUID baseStepId;
  @NonNull
  UUID policyId;
  @NonNull
  String createdBy;
}
