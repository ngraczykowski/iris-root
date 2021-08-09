package com.silenteight.payments.bridge.firco.ingress.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class IngressChannels {

  public static final String ACCEPT_ALERT_COMMAND_GATEWAY_CHANNEL =
      "acceptAlertCommandGatewayChannel";

  public static final String DELAYED_REJECT_ALERT_COMMAND_GATEWAY_CHANNEL =
      "delayedRejectAlertCommandGatewayChannel";
}
