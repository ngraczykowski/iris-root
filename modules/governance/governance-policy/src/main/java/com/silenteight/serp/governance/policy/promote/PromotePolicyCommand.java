package com.silenteight.serp.governance.policy.promote;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class PromotePolicyCommand {

  @NonNull
  UUID correlationId;
  @NonNull
  String policyName;
  @NonNull
  String promotedBy;
}
