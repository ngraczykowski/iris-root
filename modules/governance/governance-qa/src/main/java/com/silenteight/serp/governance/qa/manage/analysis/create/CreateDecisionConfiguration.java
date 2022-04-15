package com.silenteight.serp.governance.qa.manage.analysis.create;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateDecisionConfiguration {

  @Bean
  CreateDecisionUseCase createDecisionUseCase(DecisionService decisionService) {
    return new CreateDecisionUseCase(decisionService);
  }

  @Bean
  CreateAlertWithDecisionUseCase createAlertWithDecisionUseCase(DecisionService decisionService) {
    return new CreateAlertWithDecisionUseCase(decisionService);
  }
}
