package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import java.util.UUID;

public interface ResponsePublisherPort {

  void send(UUID alertMessageId);

}
