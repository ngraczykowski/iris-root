package com.silenteight.serp.governance.step.solve;

import java.util.List;

interface StepPolicyFactory {

  List<Step> getSteps();

  void reconfigure(List<Step> steps);
}
