package com.silenteight.payments.bridge.firco.ingress;

import com.silenteight.sear.payments.bridge.internal.v1.AlertMessage;

public interface AlertMessageGateway {

  void sendMessage(AlertMessage alertMessage);
}
