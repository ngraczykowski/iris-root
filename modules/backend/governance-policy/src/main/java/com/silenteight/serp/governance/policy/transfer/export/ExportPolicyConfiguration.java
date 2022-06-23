package com.silenteight.serp.governance.policy.transfer.export;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ExportPolicyConfiguration {

  @Bean
  ExportPolicyUseCase exportPolicyUseCase(PolicyExportQuery policyExportQuery) {
    return new ExportPolicyUseCase(policyExportQuery);
  }
}
