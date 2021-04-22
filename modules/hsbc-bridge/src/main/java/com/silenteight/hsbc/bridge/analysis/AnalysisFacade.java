package com.silenteight.hsbc.bridge.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Slf4j
public class AnalysisFacade {

  private final AnalysisRepository analysisRepository;
  private final Registerer registerer;
  private final Duration alertTimeoutDuration;

  @Transactional
  public AnalysisDto createAnalysisWithDataset(@NonNull String dataset) {
    var analysis = registerer.registerAnalysis(dataset);
    var entity = saveAnalysisWithTimeout(analysis);

    return entity.toAnalysisDto();
  }

  public AnalysisDto getById(long id) {
    return analysisRepository.findById(id)
        .map(AnalysisEntity::toAnalysisDto)
        .orElseThrow(() -> new AnalysisNotFoundException(id));
  }

  private AnalysisEntity saveAnalysisWithTimeout(AnalysisDto analysis) {
    var timeout = determineTimeout(analysis.getAlertCount());
    var entity = new AnalysisEntity(analysis, timeout);
    analysisRepository.save(entity);
    return entity;
  }

  private OffsetDateTime determineTimeout(long alertsCount) {
    return OffsetDateTime.now().plus(alertTimeoutDuration.multipliedBy(alertsCount));
  }
}
