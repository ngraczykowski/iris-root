package com.silenteight.serp.governance.policy.step.logic;

import java.util.List;
import java.util.UUID;

public interface PolicyStepsMatchConditionsNamesProvider {

  List<String> getMatchConditionsNames(UUID policyId);
}
