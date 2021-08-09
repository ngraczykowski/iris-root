package com.silenteight.payments.bridge.firco.ingress;

import com.silenteight.proto.payments.bridge.internal.v1.AcceptAlertCommand;

public interface AlertMessageGateway {

  void sendMessage(AcceptAlertCommand alertMessage);
}
