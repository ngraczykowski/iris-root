package com.silenteight.serp.governance.policy.step.order.edit;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Builder
@Value
class SetPolicyStepsOrderCommand {

  @NonNull
  UUID policyId;
  @NonNull
  List<UUID> stepsOrder;
  @NonNull
  String updatedBy;
}
