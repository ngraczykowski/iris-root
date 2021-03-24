package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.event.CreateAnalysisEvent;

import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
class AnalysisEventHandler {

  private final AnalysisRepository analysisRepository;

  @EventListener
  @Transactional
  public void onCreateAnalysisEvent(CreateAnalysisEvent createAnalysisEvent) {
    log.info("Received createAnalysisEvent with analysis name: "
        + createAnalysisEvent.getAnalysisName());

    AnalysisEntity analysisEntity = new AnalysisEntity(
        createAnalysisEvent.getAnalysisName(),
        createAnalysisEvent.getDatasetName(),
        createAnalysisEvent.getSolvingModelName());

    analysisRepository.save(analysisEntity);

    log.info("End processing of createAnalysisEvent. Analysis: " +
        createAnalysisEvent.getAnalysisName() + " stored.");
  }
}
