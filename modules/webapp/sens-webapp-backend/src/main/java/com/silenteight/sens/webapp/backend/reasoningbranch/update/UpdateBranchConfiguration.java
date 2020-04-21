package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateBranchConfiguration {

  @Bean
  UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase(
      ChangeRequestRepository changeRequestRepository, AuditLog auditLog) {

    return new UpdateReasoningBranchesUseCase(changeRequestRepository, auditLog);
  }
}
