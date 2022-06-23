package com.silenteight.serp.governance.policy.step.solution.list;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "serp.governance.policy-step-solution")
class PolicyStepSolutionProperties {

  private boolean hintedEnabled = false;
}
