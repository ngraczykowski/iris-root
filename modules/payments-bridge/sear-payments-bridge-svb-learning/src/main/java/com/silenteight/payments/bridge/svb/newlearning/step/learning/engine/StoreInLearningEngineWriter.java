package com.silenteight.payments.bridge.svb.newlearning.step.learning.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.engine.HistoricalDecisionLearningEnginePort;
import com.silenteight.proto.learningstore.historicaldecision.v1.api.HistoricalDecisionLearningStoreExchangeRequest;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class StoreInLearningEngineWriter
    implements ItemWriter<HistoricalDecisionLearningStoreExchangeRequest> {

  private final HistoricalDecisionLearningEnginePort learningEngineBridge;

  @Override
  public void write(
      List<? extends HistoricalDecisionLearningStoreExchangeRequest> items) {
    log.debug("Sending data with historical decisions to learning engine");
    items.forEach(
        request -> {
          if (log.isDebugEnabled()) {
            log.debug("Sending historical decision to learning engine: {}", request);
          }
          learningEngineBridge.send(request);
        });
  }
}
