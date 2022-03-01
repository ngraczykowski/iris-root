package com.silenteight.payments.bridge.svb.learning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.port.HistoricalDecisionLearningEnginePort;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class StoreInLearningEngineWriter implements ItemWriter<HistoricalDecisionLearningAggregate> {

  private final HistoricalDecisionLearningEnginePort learningEngineBridge;

  @Override
  public void write(
      List<? extends HistoricalDecisionLearningAggregate> items) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Sending data with historical decisions to learning engine chunk size:{}",
          items.size());
    }

    items.forEach(aggregate -> sendToLearningBridge(aggregate.getHistoricalFeatureRequests()));
  }

  private void sendToLearningBridge(List<HistoricalDecisionLearningStoreExchangeRequest> requests) {
    requests.forEach(learningEngineBridge::send);
  }
}
