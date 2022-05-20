package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.event.RecalculateAnalysisStatusEvent;

import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
class AnalysisEventListener {

  private final AnalysisStatusHandler statusHandler;

  @EventListener
  public void onRecalculateAnalysisStatusEvent(RecalculateAnalysisStatusEvent event) {
    log.debug("Received recalculateAnalysisStatusEvent for analysis={}", event.getAnalysisName());
    statusHandler.handleStatusChange(event.getAnalysisName());
  }
}
