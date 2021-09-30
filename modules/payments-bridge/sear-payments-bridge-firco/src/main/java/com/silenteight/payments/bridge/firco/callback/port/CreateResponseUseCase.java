package com.silenteight.payments.bridge.firco.callback.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface CreateResponseUseCase {

  void createResponse(UUID alertId, AlertMessageStatus status);

}
