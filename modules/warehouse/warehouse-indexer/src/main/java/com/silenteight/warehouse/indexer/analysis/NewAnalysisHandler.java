package com.silenteight.warehouse.indexer.analysis;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public interface NewAnalysisHandler {

  @EventListener(NewAnalysisEvent.class)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  void handle(NewAnalysisEvent event);
}
