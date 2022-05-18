package com.silenteight.payments.bridge.svb.learning.step.learning.engine;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;

import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Value
class HistoricalDecisionLearningAggregate {

  @Singular
  List<HistoricalDecisionLearningStoreExchangeRequest> historicalFeatureRequests;

}
