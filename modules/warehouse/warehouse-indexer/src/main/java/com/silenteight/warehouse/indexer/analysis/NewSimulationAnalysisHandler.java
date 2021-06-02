package com.silenteight.warehouse.indexer.analysis;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface NewSimulationAnalysisHandler {

  @EventListener(NewSimulationAnalysisEvent.class)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  void handle(NewSimulationAnalysisEvent event);
}
