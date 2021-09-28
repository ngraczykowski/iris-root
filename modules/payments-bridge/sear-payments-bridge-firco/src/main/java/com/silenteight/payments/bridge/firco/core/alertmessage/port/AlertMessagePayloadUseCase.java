package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AlertMessagePayloadUseCase {

  ObjectNode findByAlertMessageId(String alertMessageId);

}
