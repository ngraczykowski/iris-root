package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface TransitionAlertMessageStatusUseCase {

  void transitionAlertMessageStatus(UUID alertMessageId, AlertMessageStatus destinationStatus);
}
