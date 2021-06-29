package com.silenteight.serp.governance.qa.manage.analysis.update;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateAnalysisConfiguration {

  @Bean
  UpdateAnalysisDecisionUseCase updateDecisionUseCase(DecisionService decisionService) {
    return new UpdateAnalysisDecisionUseCase(decisionService);
  }
}
