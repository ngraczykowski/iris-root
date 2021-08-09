package com.silenteight.payments.bridge.firco.accept;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

public interface AlertMessageHandler {

  boolean handle(AlertMessageDto alertMessage);
}
