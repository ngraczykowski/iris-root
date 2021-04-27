package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicateFailedEvent;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;
import com.silenteight.hsbc.bridge.bulk.event.BulkProcessingStartedEvent;
import com.silenteight.hsbc.bridge.bulk.event.UpdateBulkItemStatusEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class BulkEventListener {

  private final BulkUpdater bulkUpdater;
  private final BulkItemStatusUpdater bulkItemStatusUpdater;

  @EventListener
  public void onAdjudicateFailedEvent(AdjudicateFailedEvent event) {
    bulkUpdater.updateWithError(event.getBulkId(), event.getErrorMessage());

    log.debug("AdjudicateFailedEvent handled successfully, bulkId =  {}", event.getBulkId());
  }

  @EventListener
  public void onAnalysisTimeoutEvent(AnalysisTimeoutEvent event) {
    bulkUpdater.updateWithAnalysisTimeout(event.getAnalysisId());

    log.debug("AnalysisTimeoutEvent handled, analysisId={}", event.getAnalysisId());
  }

  @EventListener
  public void onBulkProcessingStartedEvent(BulkProcessingStartedEvent event) {
    bulkUpdater.updateWithAnalysis(event.getBulkId(), event.getAnalysisId());

    log.debug("BulkProcessingStartedEvent handled successfully, bulkId =  {}", event.getBulkId());
  }

  @EventListener
  public void onUpdateBulkStatusEvent(UpdateBulkItemStatusEvent event) {
    bulkItemStatusUpdater.update(event.getBulkItemId(), event.getNewStatus());

    log.debug("UpdateBulkItemStatusEvent handled, bulkItemId={}", event.getBulkItemId());
  }
}
