package com.silenteight.serp.governance.branch;

import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
@RequiredArgsConstructor
class BranchModuleConfiguration {

  private final AuditingLogger auditingLogger;
  private final BranchRepository branchRepository;

  @Bean
  BulkChangeEventListener bulkChangeService() {
    return new BulkChangeEventListener(branchService());
  }

  @Bean
  BranchService branchService() {
    return new BranchService(auditingLogger, branchRepository);
  }
}
