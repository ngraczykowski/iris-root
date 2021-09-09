package com.silenteight.adjudication.engine.analysis.agentresponse.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AgentResponseChannels {

  /**
   * The channel which receives responses from agents.
   */
  public static final String AGENT_RESPONSE_INBOUND_CHANNEL = "agentResponseInboundChannel";

  public static final String MATCH_FEATURES_UPDATED_OUTBOUND_CHANNEL =
      "matchFeaturesUpdatedOutboundChannel";

  public static final String DELETE_AGENT_EXCHANGE_OUTBOUND_CHANNEL =
      "deleteAgentExchangeOutboundChannel";

  public static final String DELETE_AGENT_EXCHANGE_GATEWAY_CHANNEL =
      "deleteAgentExchangeInboundChannel";
}
