package com.silenteight.serp.governance.policy.step.logic;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EditStepLogicConfiguration {

  @Bean
  EditStepLogicUseCase editStepLogicUseCase(
      PolicyService policyService, PolicyStepsRequestQuery policyStepsRequestQuery) {

    return new EditStepLogicUseCase(policyService, policyStepsRequestQuery);
  }
}
