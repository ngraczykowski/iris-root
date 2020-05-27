package com.silenteight.sens.webapp.backend.changerequest.messaging;

import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandler;
import com.silenteight.sens.webapp.backend.changerequest.create.CreateChangeRequestMessageHandler;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestMessageHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChangeRequestMessagingConfiguration {

  @Bean
  CreateChangeRequestMessageGateway createChangeRequestMessageGateway(
      CreateChangeRequestMessageHandler createChangeRequestMessageHandler) {

    return new DummyCreateChangeRequestMessageGateway(createChangeRequestMessageHandler);
  }

  @Bean
  ApproveChangeRequestMessageGateway approveChangeRequestMessageGateway(
      ApproveChangeRequestMessageHandler handler) {

    return new DummyApproveChangeRequestMessageGateway(handler);
  }

  @Bean
  RejectChangeRequestMessageGateway rejectChangeRequestMessageGateway(
      RejectChangeRequestMessageHandler handler) {

    return new DummyRejectChangeRequestMessageGateway(handler);
  }
}
