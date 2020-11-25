package com.silenteight.serp.governance.branch;

import com.silenteight.auditing.bs.AuditingLogger;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackageClasses = { BranchModule.class })
public class BranchDbFixturesConfiguration {

  @Bean
  @Primary
  AuditingLogger auditingLogger() {
    return Mockito.mock(AuditingLogger.class);
  }

  @Bean
  BranchDbFixturesService branchDbFixturesService(BranchRepository branchRepository) {
    return new BranchDbFixturesService(branchRepository);
  }
}
