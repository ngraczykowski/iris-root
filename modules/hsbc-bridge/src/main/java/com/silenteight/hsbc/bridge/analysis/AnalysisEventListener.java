package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status.COMPLETED;

@RequiredArgsConstructor
class AnalysisEventListener {

  private final AnalysisRepository repository;

  @EventListener
  @Transactional
  public void onAnalysisCompletedEvent(AnalysisCompletedEvent event) {
    repository.findByName(event.getAnalysis()).ifPresent(analysis -> {
      analysis.setStatus(COMPLETED);
      repository.save(analysis);
    });
  }
}
