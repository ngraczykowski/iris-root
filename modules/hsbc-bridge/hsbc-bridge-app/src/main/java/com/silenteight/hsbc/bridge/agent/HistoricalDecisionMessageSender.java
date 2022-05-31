package com.silenteight.hsbc.bridge.agent;

import com.silenteight.proto.learningstore.historicaldecision.v2.api.HistoricalDecisionLearningStoreExchangeRequest;

public interface HistoricalDecisionMessageSender {

  void send(HistoricalDecisionLearningStoreExchangeRequest historicalDecisionLearningStoreExchangeRequest);
}
