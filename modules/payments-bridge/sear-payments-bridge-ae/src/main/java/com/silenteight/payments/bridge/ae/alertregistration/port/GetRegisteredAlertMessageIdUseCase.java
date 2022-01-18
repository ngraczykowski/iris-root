package com.silenteight.payments.bridge.ae.alertregistration.port;

import java.util.UUID;

public interface GetRegisteredAlertMessageIdUseCase {

  UUID getAlertMessageId(String alertName);
}
