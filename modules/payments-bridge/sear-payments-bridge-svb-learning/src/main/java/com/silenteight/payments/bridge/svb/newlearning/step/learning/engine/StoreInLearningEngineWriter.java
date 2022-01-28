package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.engine.HistoricalDecisionLearningEnginePort;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class StoreInLearningEngineWriter implements ItemWriter<HistoricalDecisionLearningAggregate> {

  private final HistoricalDecisionLearningEnginePort learningEngineBridge;

  @Override
  public void write(
      List<? extends HistoricalDecisionLearningAggregate> items) {
    log.debug(
        "Sending data with historical decisions to learning engine chunk size:{}",
        items.size());
    items.forEach(aggregate -> {
      if (log.isDebugEnabled()) {
        log.debug(
            "Sending historical decision to learning engine with aggregate size: {}",
            aggregate.getHistoricalFeatureRequests().size());
      }
      aggregate.getHistoricalFeatureRequests().forEach(learningEngineBridge::send);
    });
  }
}
