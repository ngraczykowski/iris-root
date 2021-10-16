package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface NotifyResponseCompletedUseCase {

  void notify(UUID alertId, UUID recommendationId, AlertMessageStatus status);
}
