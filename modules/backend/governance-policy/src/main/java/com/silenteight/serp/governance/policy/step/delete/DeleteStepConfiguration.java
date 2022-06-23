package com.silenteight.serp.governance.policy.step.delete;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteStepConfiguration {

  @Bean
  DeleteStepUseCase deleteStepUseCase(
      PolicyService policyService, PolicyStepsRequestQuery policyStepsRequestQuery) {
    return new DeleteStepUseCase(policyService, policyStepsRequestQuery);
  }
}
