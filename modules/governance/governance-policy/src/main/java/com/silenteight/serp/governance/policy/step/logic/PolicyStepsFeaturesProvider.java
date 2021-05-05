package com.silenteight.serp.governance.policy.step.logic;

import java.util.List;
import java.util.UUID;

public interface PolicyStepsFeaturesProvider {

  List<String> getFeatures(UUID policyId);
}
