package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.AlertMessageStoredPublisherPort;

import org.springframework.stereotype.Component;

@Component
class DummyAlertMessageStoredPublisherPort implements AlertMessageStoredPublisherPort {

  @Override
  public boolean publish(
      FircoAlertMessage message) {
    return false;
  }
}
