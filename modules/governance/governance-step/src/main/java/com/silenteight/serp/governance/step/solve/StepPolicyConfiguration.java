package com.silenteight.serp.governance.step.solve;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
class StepPolicyConfiguration {

  @Bean
  StepService stepService(StepPolicyFactory stepPolicyFactory) {
    return new StepService(stepPolicyFactory);
  }

  @Bean
  StepPolicyFactory stepPolicyFactory() {
    return new StepPolicyFactory() {

      @Override
      public List<Step> getSteps() {
        return new ArrayList<>();
      }

      @Override
      public void reconfigure(List<Step> steps) {

      }
    };
  }
}
