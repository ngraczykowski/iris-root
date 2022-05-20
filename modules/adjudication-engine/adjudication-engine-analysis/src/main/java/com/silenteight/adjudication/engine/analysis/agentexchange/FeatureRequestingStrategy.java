package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.AgentExchangeRequestMessage;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import java.util.function.Consumer;

public interface FeatureRequestingStrategy {

  void createRequests(
      MissingMatchFeatureChunk chunk, Consumer<AgentExchangeRequestMessage> messageConsumer);

  void flush(Consumer<AgentExchangeRequestMessage> messageConsumer);
}
