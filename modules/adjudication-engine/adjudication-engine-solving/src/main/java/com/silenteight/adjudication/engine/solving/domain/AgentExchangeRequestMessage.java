package com.silenteight.adjudication.engine.solving.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import java.util.UUID;

@Value
@Builder
public class AgentExchangeRequestMessage {

  public static final int MIN_PRIORITY = 1;
  public static final int MAX_PRIORITY = 10;

  @NonNull
  UUID requestId;

  @NonNull
  String agentConfig;

  int priority;

  AgentExchangeRequest agentExchangeRequest;

  public int getMatchesCount() {
    return agentExchangeRequest.getMatchesCount();
  }
}
