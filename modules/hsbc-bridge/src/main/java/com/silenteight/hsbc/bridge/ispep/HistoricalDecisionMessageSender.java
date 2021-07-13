package com.silenteight.hsbc.bridge.ispep;

import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

public interface HistoricalDecisionMessageSender {

  void send(HistoricalDecisionLearningStoreExchangeRequest historicalDecisionLearningStoreExchangeRequest);
}
