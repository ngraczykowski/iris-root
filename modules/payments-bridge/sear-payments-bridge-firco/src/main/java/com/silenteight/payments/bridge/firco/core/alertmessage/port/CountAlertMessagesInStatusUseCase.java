package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

public interface CountAlertMessagesInStatusUseCase {

  long countAlertMessagesInStatus(AlertMessageStatus status);
}
