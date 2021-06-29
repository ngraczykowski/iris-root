package com.silenteight.serp.governance.qa.manage.validation.view;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ViewValidationConfiguration {

  @Bean
  ViewAlertValidationUseCase viewValidationAlertUseCase(DecisionService decisionService) {
    return new ViewAlertValidationUseCase(decisionService);
  }
}
