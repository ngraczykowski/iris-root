package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.common.model.AlertRegistration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CheckAlertRegisteredPort {

  @Gateway(requestChannel = "fircoAlertRegisteredRequestChannel",
      replyChannel = "fircoAlertRegisteredResponseChannel")
  boolean isAlertRegistered(AlertRegistration alertRegistration);

}
