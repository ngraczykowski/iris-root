package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class BulkEventListener {

  private final BulkUpdater bulkUpdater;

  @EventListener
  public void onAnalysisTimeoutEvent(AnalysisTimeoutEvent event) {
    bulkUpdater.updateWithAnalysisTimeout(event.getAnalysisId());

    log.debug("AnalysisTimeoutEvent handled, analysisId={}", event.getAnalysisId());
  }
}
