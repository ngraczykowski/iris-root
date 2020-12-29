package com.silenteight.serp.governance.policy.solve;

import java.util.List;

interface StepPolicyFactory {

  List<Step> getSteps();

  void reconfigure(List<Step> steps);
}
