package com.silenteight.serp.governance.model.transfer.importing;

import com.silenteight.serp.governance.model.domain.ModelService;
import com.silenteight.serp.governance.policy.transfer.importing.ImportPolicyUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ImportModelConfiguration {

  @Bean
  ImportModelUseCase importModelUseCase(
      ImportPolicyUseCase importPolicyUseCase, ModelService modelService) {

    return new ImportModelUseCase(importPolicyUseCase, modelService);
  }
}
