package com.silenteight.serp.governance.policy.clone;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
class ClonePolicyCommand {

  @NonNull
  UUID id;
  @NonNull
  String createdBy;
  @NonNull
  UUID basePolicyId;
}
