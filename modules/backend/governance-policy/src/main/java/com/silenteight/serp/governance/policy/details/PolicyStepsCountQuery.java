package com.silenteight.serp.governance.policy.details;

import java.util.UUID;

public interface PolicyStepsCountQuery {

  long getStepsCount(UUID policyId);
}
