package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateBranchConfiguration {

  @Bean
  UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase(
      ChangeRequestRepository changeRequestRepository, AuditTracer auditTracer) {

    return new UpdateReasoningBranchesUseCase(changeRequestRepository, auditTracer);
  }
}
