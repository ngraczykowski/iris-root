package com.silenteight.sens.webapp.common.support.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AuditConfiguration {

  @Bean
  public AuditReaderProvider auditReaderProvider(EntityManager entityManager) {
    return new AuditReaderProvider(entityManager);
  }
}

