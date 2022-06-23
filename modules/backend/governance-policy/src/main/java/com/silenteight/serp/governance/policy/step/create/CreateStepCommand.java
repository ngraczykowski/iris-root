package com.silenteight.serp.governance.policy.step.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.Solution;

import java.util.UUID;
import javax.annotation.Nullable;

@Builder
@Value
class CreateStepCommand {

  @NonNull
  UUID policyId;
  @NonNull
  UUID stepId;
  @NonNull
  String name;
  @Nullable
  String description;
  @NonNull
  Solution solution;
  @NonNull
  String createdBy;
  @NonNull
  StepType stepType;
}
