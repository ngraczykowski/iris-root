package com.silenteight.serp.governance.bulkchange.audit;

import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuditBulkChangeConfiguration {

  private final AuditingLogger auditingLogger;

  @Bean
  BulkChangeAuditable bulkChangeAuditable() {
    return new BulkChangeAudit(
        auditBulkChangeApplicationHandler(),
        auditBulkChangeCreationHandler(),
        auditBulkChangeRejectionHandler()
    );
  }

  @Bean
  AuditBulkChangeRejectionHandler auditBulkChangeRejectionHandler() {
    return new AuditBulkChangeRejectionHandler(auditingLogger);
  }

  @Bean
  AuditBulkChangeCreationHandler auditBulkChangeCreationHandler() {
    return new AuditBulkChangeCreationHandler(auditingLogger);
  }

  @Bean
  AuditBulkChangeApplicationHandler auditBulkChangeApplicationHandler() {
    return new AuditBulkChangeApplicationHandler(auditingLogger);
  }
}
