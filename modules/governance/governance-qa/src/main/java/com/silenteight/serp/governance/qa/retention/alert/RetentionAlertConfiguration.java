package com.silenteight.serp.governance.qa.retention.alert;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
import com.silenteight.serp.governance.qa.manage.domain.AlertService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AlertExpiredProperties.class)
class RetentionAlertConfiguration {

  @Bean
  EraseAlertUseCase eraseAlertUseCase(
      AlertQuery alertQuery,
      AlertService alertService,
      @Valid AlertExpiredProperties properties) {

    return new EraseAlertUseCase(alertQuery, alertService, properties.getBatchSize());
  }
}
