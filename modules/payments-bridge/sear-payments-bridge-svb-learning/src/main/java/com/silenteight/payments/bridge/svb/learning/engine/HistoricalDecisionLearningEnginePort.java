package com.silenteight.payments.bridge.svb.learning.engine;

import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

public interface HistoricalDecisionLearningEnginePort {

  void send(HistoricalDecisionLearningStoreExchangeRequest storeRequest);

}
