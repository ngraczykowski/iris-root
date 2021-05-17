package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan
@EnableJpaRepositories
@EnableTransactionManagement
class PolicyDomainConfiguration {

  @Bean
  PolicyService policyService(
      PolicyRepository policyRepository,
      AuditingLogger auditingLogger,
      ApplicationEventPublisher eventPublisher) {

    return new PolicyService(policyRepository, auditingLogger, eventPublisher);
  }

  @Bean
  PolicyImportedEventHandler policyCreatedEventHandler(
      AuditingLogger auditingLogger, ApplicationEventPublisher eventPublisher) {

    return new PolicyImportedEventHandler(auditingLogger, eventPublisher);
  }

  @Bean
  PolicyQuery policyQuery(PolicyRepository policyRepository) {
    return new PolicyQuery(policyRepository);
  }

  @Bean
  StepQuery stepQuery(StepRepository stepRepository, PolicyRepository policyRepository) {
    return new StepQuery(stepRepository, policyRepository);
  }

  @Bean
  FeaturesLogicQuery featuresLogicQuery(
      StepRepository stepRepository,
      FeatureLogicRepository featureLogicRepository) {

    return new FeaturesLogicQuery(stepRepository, featureLogicRepository);
  }
}
