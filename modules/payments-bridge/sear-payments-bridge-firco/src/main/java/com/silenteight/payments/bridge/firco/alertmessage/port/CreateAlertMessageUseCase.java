package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;

public interface CreateAlertMessageUseCase {

  void createAlertMessage(FircoAlertMessage alertModel);
}
