package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

public interface AlertMessageUseCase {

  AlertMessageModel findByAlertMessageId(String alertMessageId);

}
