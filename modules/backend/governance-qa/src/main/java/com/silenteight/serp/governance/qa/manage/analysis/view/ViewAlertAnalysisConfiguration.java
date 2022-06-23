package com.silenteight.serp.governance.qa.manage.analysis.view;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ViewAlertAnalysisConfiguration {

  @Bean
  ViewAlertAnalysisUseCase viewAlertAnalysisUseCase(DecisionService decisionService) {
    return new ViewAlertAnalysisUseCase(decisionService);
  }
}
