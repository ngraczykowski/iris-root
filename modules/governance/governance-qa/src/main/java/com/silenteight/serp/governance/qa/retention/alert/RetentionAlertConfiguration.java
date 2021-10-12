package com.silenteight.serp.governance.qa.retention.alert;

import com.silenteight.serp.governance.qa.manage.domain.AlertService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RetentionAlertConfiguration {

  @Bean
  EraseAlertUseCase eraseAlertUseCase(AlertService alertService) {
    return new EraseAlertUseCase(alertService);
  }
}
