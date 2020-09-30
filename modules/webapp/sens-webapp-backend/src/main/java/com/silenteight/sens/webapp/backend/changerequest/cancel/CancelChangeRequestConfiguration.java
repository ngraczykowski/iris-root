package com.silenteight.sens.webapp.backend.changerequest.cancel;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CancelChangeRequestConfiguration {

  @Bean
  CancelChangeRequestUseCase cancelChangeRequestUseCase(
      AuditTracer auditTracer, CancelChangeRequestMessageGateway messageGateway) {

    return new CancelChangeRequestUseCase(auditTracer, messageGateway);
  }

  @Bean
  CancelChangeRequestMessageHandler cancelChangeRequestMessageHandler(
      ChangeRequestService changeRequestService) {

    return new CancelChangeRequestMessageHandler(changeRequestService);
  }
}
