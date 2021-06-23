package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.event.RecalculateAnalysisStatusEvent;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
class AnalysisEventListener {

  private final AnalysisStatusHandler statusHandler;

  @EventListener
  public void onRecalculateAnalysisStatusEvent(RecalculateAnalysisStatusEvent event) {
    statusHandler.handleStatusChange(event.getAnalysisName());
  }
}
