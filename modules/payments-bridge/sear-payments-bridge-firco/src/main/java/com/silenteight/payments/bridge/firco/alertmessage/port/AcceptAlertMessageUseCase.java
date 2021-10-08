package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertId;

public interface AcceptAlertMessageUseCase {

  boolean test(AlertId alert);

}
