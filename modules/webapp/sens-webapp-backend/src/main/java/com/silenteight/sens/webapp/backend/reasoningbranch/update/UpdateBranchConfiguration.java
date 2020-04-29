package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateBranchConfiguration {

  @Bean
  UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase(
      ChangeRequestRepository changeRequestRepository) {

    return new UpdateReasoningBranchesUseCase(changeRequestRepository);
  }
}
