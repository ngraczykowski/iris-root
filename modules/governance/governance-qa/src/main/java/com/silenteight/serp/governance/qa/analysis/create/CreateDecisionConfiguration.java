package com.silenteight.serp.governance.qa.analysis.create;

import com.silenteight.serp.governance.qa.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateDecisionConfiguration {

  @Bean
  CreateDecisionUseCase createDecisionUseCase(DecisionService decisionService) {
    return new CreateDecisionUseCase(decisionService);
  }
}
