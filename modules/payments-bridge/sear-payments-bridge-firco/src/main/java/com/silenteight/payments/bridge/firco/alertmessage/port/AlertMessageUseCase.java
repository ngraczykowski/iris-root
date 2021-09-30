package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertData;

public interface AlertMessageUseCase {

  AlertData findByAlertMessageId(String alertMessageId);

}
