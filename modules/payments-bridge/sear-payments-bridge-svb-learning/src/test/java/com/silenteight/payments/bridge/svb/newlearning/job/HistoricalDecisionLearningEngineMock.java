package com.silenteight.payments.bridge.svb.newlearning.job;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.engine.HistoricalDecisionLearningEnginePort;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

import java.util.stream.Collectors;

@Slf4j
class HistoricalDecisionLearningEngineMock implements HistoricalDecisionLearningEnginePort {

  @Override
  public void send(
      HistoricalDecisionLearningStoreExchangeRequest storeRequest) {
    var alerts = storeRequest
        .getAlertsList()
        .stream()
        .collect(Collectors.toList());
    log.info("Sending data to learning engine with alertIds: {}", alerts);
  }
}
