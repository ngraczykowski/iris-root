package com.silenteight.payments.bridge.firco.ingress;

import com.silenteight.proto.payments.bridge.internal.v1.RejectAlertCommand;

public interface DelayedRejectAlertGateway {

  void sendMessage(RejectAlertCommand alertMessage);
}
