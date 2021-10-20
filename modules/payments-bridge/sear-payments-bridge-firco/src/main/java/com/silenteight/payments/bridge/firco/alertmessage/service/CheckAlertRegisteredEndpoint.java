package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertRegistration;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_REQUEST_CHANNEL;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_RESPONSE_CHANNEL;

@MessageEndpoint
@RequiredArgsConstructor
class CheckAlertRegisteredEndpoint {

  private final AlertMessageRepository repository;

  @ServiceActivator(inputChannel = ALERT_REGISTERED_REQUEST_CHANNEL,
      outputChannel = ALERT_REGISTERED_RESPONSE_CHANNEL)
  boolean apply(AlertRegistration alertRegistration) {
    return repository.existsByMessageIdAndSystemId(
        alertRegistration.getMessageId(), alertRegistration.getSystemId());
  }

}
