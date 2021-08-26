package com.silenteight.adjudication.engine.analysis.agentexchange.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class DeleteAgentExchangeRequest {

  UUID agentExchangeRequestId;

  long matchId;

  List<String> featuresIds;
}
