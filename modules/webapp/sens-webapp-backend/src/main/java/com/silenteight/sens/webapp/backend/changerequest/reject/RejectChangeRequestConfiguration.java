package com.silenteight.sens.webapp.backend.changerequest.reject;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;
import com.silenteight.sens.webapp.backend.changerequest.messaging.RejectChangeRequestMessageSender;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RejectChangeRequestConfiguration {

  @Bean
  RejectChangeRequestUseCase rejectChangeRequestUseCase(
      AuditTracer auditTracer, RejectChangeRequestMessageSender messageSender) {

    return new RejectChangeRequestUseCase(auditTracer, messageSender);
  }

  @Bean
  RejectChangeRequestMessageHandler rejectChangeRequestMessageHandler(
      ChangeRequestService changeRequestService) {

    return new RejectChangeRequestMessageHandler(changeRequestService);
  }
}
