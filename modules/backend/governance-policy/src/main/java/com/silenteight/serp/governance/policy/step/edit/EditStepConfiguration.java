package com.silenteight.serp.governance.policy.step.edit;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EditStepConfiguration {

  @Bean
  EditStepUseCase editStepUseCase(
      PolicyService policyService, PolicyStepsRequestQuery policyStepsRequestQuery) {
    return new EditStepUseCase(policyService, policyStepsRequestQuery);
  }
}
