package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateBranchConfiguration {

  @Bean
  UpdateReasoningBranchUseCase updateReasoningBranchUseCase(
      ReasoningBranchUpdateRepository reasoningBranchUpdateRepository) {
    return new UpdateReasoningBranchUseCase(reasoningBranchUpdateRepository);
  }
}
