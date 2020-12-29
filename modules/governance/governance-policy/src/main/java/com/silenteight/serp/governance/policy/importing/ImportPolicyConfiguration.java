package com.silenteight.serp.governance.policy.importing;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ImportPolicyConfiguration {

  @Bean
  ImportPolicyUseCase importPolicyUseCase(
      ImportedPolicyRootParser importedPolicyRootParser, PolicyService policyService) {

    return new ImportPolicyUseCase(importedPolicyRootParser, policyService);
  }

  @Bean
  ImportedPolicyRootParser importedPolicyRootParser() {
    return new ImportedPolicyRootParser();
  }
}
