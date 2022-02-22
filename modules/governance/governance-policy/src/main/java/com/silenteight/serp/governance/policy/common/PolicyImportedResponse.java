package com.silenteight.serp.governance.policy.common;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class PolicyImportedResponse {

  @NonNull
  UUID policyId;
}
