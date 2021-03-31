package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationFacade;
import com.silenteight.hsbc.bridge.bulk.event.BulkPreProcessingFinishedEvent;
import com.silenteight.hsbc.bridge.model.ModelUseCase;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
class BulkPreProcessingFinishedEventHandler {

  private final AdjudicationFacade adjudicationFacade;
  private final ModelUseCase modelUseCase;

  @EventListener
  @Async
  public void onBulkPreProcessingFinishedEvent(
      BulkPreProcessingFinishedEvent bulkPreProcessingFinishedEvent) {
    log.info("Received bulkPreProcessingFinishedEvent");

    String analysisId = adjudicationFacade.createAnalysis(
        bulkPreProcessingFinishedEvent.getAlertMatchIdComposites(), modelUseCase);

    log.info("Analysis created with id: " + analysisId);
  }
}
