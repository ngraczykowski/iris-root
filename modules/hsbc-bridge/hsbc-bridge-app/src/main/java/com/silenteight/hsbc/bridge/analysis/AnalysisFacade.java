package com.silenteight.hsbc.bridge.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;

@RequiredArgsConstructor
@Slf4j
public class AnalysisFacade {

  private final AnalysisRepository analysisRepository;
  private final Registerer registerer;
  private final AnalysisTimeoutCalculator analysisTimeoutCalculator;

  public AnalysisDto createAnalysisWithDataset(@NonNull String dataset) {
    var analysis = registerer.registerAnalysis(dataset);
    var entity = saveAnalysisWithTimeout(analysis);

    return entity.toAnalysisDto();
  }

  private AnalysisEntity saveAnalysisWithTimeout(AnalysisDto analysis) {
    var timeout = analysisTimeoutCalculator.determineTimeout(analysis.getAlertCount());
    var entity = new AnalysisEntity(analysis, timeout);

    analysisRepository.save(entity);
    return entity;
  }
}
