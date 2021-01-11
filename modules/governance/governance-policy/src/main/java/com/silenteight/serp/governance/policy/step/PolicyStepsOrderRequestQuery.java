package com.silenteight.serp.governance.policy.step;

import java.util.List;
import java.util.UUID;

public interface PolicyStepsOrderRequestQuery {

  List<UUID> listStepsOrder(UUID policyId);
}
