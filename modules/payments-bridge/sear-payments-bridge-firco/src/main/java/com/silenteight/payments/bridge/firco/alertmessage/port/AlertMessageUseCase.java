package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertData;

import java.util.UUID;

public interface AlertMessageUseCase {

  AlertData findByAlertMessageId(UUID alertMessageId);

}
