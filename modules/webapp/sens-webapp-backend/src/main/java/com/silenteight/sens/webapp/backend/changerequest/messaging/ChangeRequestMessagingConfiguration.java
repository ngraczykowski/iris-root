package com.silenteight.sens.webapp.backend.changerequest.messaging;

import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestMessageHandler;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestMessageHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChangeRequestMessagingConfiguration {

  @Bean
  ApproveChangeRequestMessageSender approveChangeRequestMessageSender(
      ApproveChangeRequestMessageHandler handler) {
    
    return new DummyApproveChangeRequestMessageSender(handler);
  }

  @Bean
  RejectChangeRequestMessageSender rejectChangeRequestMessageSender(
      RejectChangeRequestMessageHandler handler) {

    return new DummyRejectChangeRequestMessageSender(handler);
  }
}
