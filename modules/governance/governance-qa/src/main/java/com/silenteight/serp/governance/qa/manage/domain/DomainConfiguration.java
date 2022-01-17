package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.qa.send.SendAlertMessageUseCase;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
class DomainConfiguration {

  @Bean
  AlertAnalysisQuery alertAnalysisQuery(AlertRepository alertRepository,
      DecisionRepository decisionRepository) {

    return new AlertAnalysisQuery(alertRepository, decisionRepository);
  }

  @Bean
  AlertValidationQuery alertValidationQuery(DecisionRepository decisionRepository) {

    return new AlertValidationQuery(decisionRepository);
  }

  @Bean
  AlertQuery alertQuery(AlertRepository alertRepository) {
    return new AlertQuery(alertRepository);
  }

  @Bean
  DecisionService decisionService(AlertRepository alertRepository,
      DecisionRepository decisionRepository,
      AuditingLogger auditingLogger,
      SendAlertMessageUseCase sendAlertMessageUseCase) {

    return new DecisionService(
        alertRepository, decisionRepository, auditingLogger, sendAlertMessageUseCase);
  }

  @Bean
  AlertService alertService(AlertRepository alertRepository, AuditingLogger auditingLogger) {
    return new AlertService(alertRepository, auditingLogger);
  }
}
