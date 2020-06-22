package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ValidateBranchConfiguration {

  @Bean
  ReasoningBranchValidator reasoningBranchValidator(
      ReasoningBranchesValidateQuery reasoningBranchesValidateQuery) {
    return new ReasoningBranchValidator(reasoningBranchesValidateQuery);
  }
}
