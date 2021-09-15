package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.ResponsePublisherPort;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class DummyResponsePublisherPort implements ResponsePublisherPort  {

  @Override
  public void send(UUID alertMessageId) {

  }
}
