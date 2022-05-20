package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AgentExchangeChannels {

  /**
   * This channel receives PendingRecommendations message, triggering evaluation of missing match
   * features and sending requests to agents for these features.
   */
  public static final String AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL =
      "agentExchangePendingRecommendationsInboundChannel";

  public static final String DELETE_AGENT_EXCHANGE_INBOUND_CHANNEL =
      "deleteAgentExchangeInboundChannel";

  static final String AGENT_EXCHANGE_REQUEST_GATEWAY_CHANNEL =
      "deleteAgentExchangeRequestGatewayChannel";

  /**
   * This channel is for sending AgentExchangeRequest messages to agents.
   */
  public static final String AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL =
      "agentExchangeRequestOutboundChannel";

  /**
   * Header name with agent configuration name.
   */
  public static final String AGENT_CONFIG_HEADER = "agentConfig";
}
