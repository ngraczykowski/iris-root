package com.silenteight.adjudication.engine.analysis.agentexchange.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AgentExchangeChannels {

  /**
   * This channel receives PendingRecommendations message, triggering
   * evaluation of missing match features and sending requests to agents for
   * these features.
   */
  public static final String AGENT_EXCHANGE_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL =
      "agentExchangePendingRecommendationsInboundChannel";

  /**
   * This channel is for sending AgentExchangeRequest messages to agents.
   */
  public static final String AGENT_EXCHANGE_REQUEST_OUTBOUND_CHANNEL =
      "agentExchangeRequestOutboundChannel";

  /**
   * The channel which receives responses from agents.
   */
  static final String AGENT_EXCHANGE_RESPONSE_INBOUND_CHANNEL =
      "agentExchangeResponseInboundChannel";
}
