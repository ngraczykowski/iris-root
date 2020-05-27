package com.silenteight.sens.webapp.backend.changerequest.approve;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;
import com.silenteight.sens.webapp.backend.changerequest.messaging.ApproveChangeRequestMessageGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApproveChangeRequestConfiguration {

  @Bean
  ApproveChangeRequestUseCase approveChangeRequestUseCase(
      AuditTracer auditTracer, ApproveChangeRequestMessageGateway messageGateway) {

    return new ApproveChangeRequestUseCase(auditTracer, messageGateway);
  }

  @Bean
  ApproveChangeRequestMessageHandler approveChangeRequestMessageHandler(
      ChangeRequestService changeRequestService) {

    return new ApproveChangeRequestMessageHandler(changeRequestService);
  }
}
