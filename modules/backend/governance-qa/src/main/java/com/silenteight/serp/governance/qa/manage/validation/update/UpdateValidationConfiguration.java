package com.silenteight.serp.governance.qa.manage.validation.update;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateValidationConfiguration {

  @Bean
  UpdateValidationDecisionUseCase updateValidationDecisionUseCase(DecisionService decisionService) {
    return new UpdateValidationDecisionUseCase(decisionService);
  }
}
