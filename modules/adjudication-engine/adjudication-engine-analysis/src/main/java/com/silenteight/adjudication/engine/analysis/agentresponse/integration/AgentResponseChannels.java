package com.silenteight.adjudication.engine.analysis.agentresponse.integration;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AgentResponseChannels {

  /**
   * The channel which receives responses from agents.
   */
  public static final String AGENT_RESPONSE_INBOUND_CHANNEL = "agentResponseInboundChannel";
}
