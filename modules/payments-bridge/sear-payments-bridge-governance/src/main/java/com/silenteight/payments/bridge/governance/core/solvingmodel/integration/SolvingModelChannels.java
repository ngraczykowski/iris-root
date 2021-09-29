package com.silenteight.payments.bridge.governance.core.solvingmodel.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SolvingModelChannels {

  public static final String SOLVING_MODEL_GATEWAY_CHANNEL =
      "solvingModelGatewayChannel";

  public static final String SOLVING_MODEL_OUTBOUND_CHANNEL =
      "solvingModelOutboundChannel";
}
