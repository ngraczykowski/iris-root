package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class PolicyDomainConfiguration {

  @Bean
  PolicyService policyService(
      PolicyRepository policyRepository,
      StepRepository stepRepository,
      AuditingLogger auditingLogger,
      ApplicationEventPublisher eventPublisher) {

    return new PolicyService(policyRepository, stepRepository, auditingLogger, eventPublisher);
  }

  @Bean
  PolicyCreatedEventHandler policyCreatedEventHandler(
      AuditingLogger auditingLogger, ApplicationEventPublisher eventPublisher) {

    return new PolicyCreatedEventHandler(auditingLogger, eventPublisher);
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
      StepRepository stepRepository, FeatureLogicRepository featureLogicRepository) {
    return new FeaturesLogicQuery(stepRepository, featureLogicRepository);
  }
}
