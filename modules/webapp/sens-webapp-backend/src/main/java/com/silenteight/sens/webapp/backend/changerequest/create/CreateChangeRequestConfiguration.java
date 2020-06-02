package com.silenteight.sens.webapp.backend.changerequest.create;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateChangeRequestConfiguration {

  @Bean
  CreateChangeRequestUseCase createChangeRequestUseCase(
      AuditTracer auditTracer, CreateChangeRequestMessageGateway messageGateway) {

    return new CreateChangeRequestUseCase(auditTracer, messageGateway);
  }

  @Bean
  CreateChangeRequestMessageHandler createChangeRequestMessageHandler(
      ChangeRequestService changeRequestService) {
    return new CreateChangeRequestMessageHandler(changeRequestService);
  }
}
