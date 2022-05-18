package com.silenteight.payments.bridge.svb.learning.port;

import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

public interface HistoricalDecisionLearningEnginePort {

  void send(HistoricalDecisionLearningStoreExchangeRequest storeRequest);

}
