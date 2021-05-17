package com.silenteight.serp.governance.model.domain;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class ModelDomainConfiguration {

  @Bean
  ModelService modelService(ModelRepository modelRepository, AuditingLogger auditingLogger) {
    return new ModelService(modelRepository, auditingLogger);
  }

  @Bean
  ModelQuery modelQuery(
      ModelRepository modelRepository, CurrentPolicyProvider currentPolicyProvider) {

    return new ModelQuery(modelRepository, currentPolicyProvider);
  }
}
