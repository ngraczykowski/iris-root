package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

public interface AlertMessageUseCase {

  AlertMessageModel findByAlertMessageId(String alertMessageId);

}
