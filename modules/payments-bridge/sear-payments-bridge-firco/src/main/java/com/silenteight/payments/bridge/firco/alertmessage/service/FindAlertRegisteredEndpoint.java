package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.common.model.RegisteredAlert;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_REQUEST_CHANNEL;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REGISTERED_RESPONSE_CHANNEL;

@MessageEndpoint
@RequiredArgsConstructor
class FindAlertRegisteredEndpoint {

  private final AlertRegisteredJdbcDataAccess alertRegisteredJdbcDataAccess;

  @ServiceActivator(inputChannel = ALERT_REGISTERED_REQUEST_CHANNEL,
      outputChannel = ALERT_REGISTERED_RESPONSE_CHANNEL)
  List<RegisteredAlert> apply(List<FindRegisteredAlertRequest> registeredAlert) {
    return alertRegisteredJdbcDataAccess.findRegistered(registeredAlert);
  }

}
