package com.silenteight.serp.governance.policy.transfer.importing;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ImportPolicyConfiguration {

  @Bean
  ImportPolicyUseCase importPolicyUseCase(
      TransferredPolicyRootDtoParser transferredPolicyRootParser, PolicyService policyService) {

    return new ImportPolicyUseCase(transferredPolicyRootParser, policyService);
  }

  @Bean
  TransferredPolicyRootDtoParser transferredPolicyRootParser() {
    return new TransferredPolicyRootDtoParser();
  }
}
