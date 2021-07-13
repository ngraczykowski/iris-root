package com.silenteight.hsbc.bridge.agent;

import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

public interface HistoricalDecisionMessageSender {

  void send(HistoricalDecisionLearningStoreExchangeRequest historicalDecisionLearningStoreExchangeRequest);
}
