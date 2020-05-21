package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.ReasoningBranchValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UpdateBranchConfiguration {

  @Bean
  UpdateReasoningBranchesUseCase updateReasoningBranchesUseCase(
      ChangeRequestRepository changeRequestRepository,
      ReasoningBranchValidator reasoningBranchValidator,
      AuditTracer auditTracer) {

    return new UpdateReasoningBranchesUseCase(
        changeRequestRepository, reasoningBranchValidator, auditTracer);
  }
}
