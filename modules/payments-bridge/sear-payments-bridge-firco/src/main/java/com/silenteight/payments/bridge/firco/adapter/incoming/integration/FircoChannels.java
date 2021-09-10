package com.silenteight.payments.bridge.firco.adapter.incoming.integration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FircoChannels {

  public static final String FIRCO_COMMANDS_INBOUND_CHANNEL = "fircoCommandsInboundChannel";

  static final String FIRCO_ACCEPT_ALERT_COMMAND_INBOUND_CHANNEL =
      "fircoAcceptAlertCommandInboundChannel";
  static final String FIRCO_REJECT_ALERT_COMMAND_INBOUND_CHANNEL =
      "fircoRejectAlertCommandInboundChannel";
  static final String FIRCO_RECOMMEND_ALERT_COMMAND_INBOUND_CHANNEL =
      "fircoRecommendAlertCommandInboundChannel";
}
