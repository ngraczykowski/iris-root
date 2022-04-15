package com.silenteight.serp.governance.qa.retention.personalinformation;

import com.silenteight.serp.governance.qa.manage.domain.AlertQuery;
import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(PersonalInformationProperties.class)
class RetentionEraseConfiguration {

  @Bean
  EraseDecisionCommentUseCase eraseDecisionCommentUseCase(
      AlertQuery alertQuery,
      DecisionService decisionService,
      @Valid PersonalInformationProperties properties) {

    return new EraseDecisionCommentUseCase(alertQuery, decisionService, properties.getBatchSize());
  }
}
