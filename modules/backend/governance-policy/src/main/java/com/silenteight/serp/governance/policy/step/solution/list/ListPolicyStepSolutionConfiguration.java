package com.silenteight.serp.governance.policy.step.solution.list;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PolicyStepSolutionProperties.class)
class ListPolicyStepSolutionConfiguration {

  @Bean
  PolicyStepSolutionQuery policyStepSolutionQuery(PolicyStepSolutionProperties properties) {
    return new PolicyStepSolutionQuery(properties.isHintedEnabled());
  }
}
