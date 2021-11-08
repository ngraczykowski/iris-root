package com.silenteight.hsbc.bridge.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;
import com.silenteight.hsbc.bridge.analysis.dto.GetAnalysisResponseDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
class AnalysisStatusCalculator {

  private final AnalysisServiceClient analysisServiceClient;
  private final AnalysisRepository repository;

  @Transactional
  public Optional<AnalysisEntity.Status> recalculateStatus(@NonNull String name) {
    var entity = repository.findByName(name);

    if (entity.isEmpty()) {
      log.error("Analysis: {} does not exist", name);
      return Optional.empty();
    }

    var analysis = entity.get();
    if (analysis.isInProgress() && hasNoPendingAlerts(name)) {
      analysis.setStatus(Status.COMPLETED);
      repository.save(analysis);
    }

    return Optional.of(analysis.getStatus());
  }

  private boolean hasNoPendingAlerts(String name) {
    var result = safeGetAnalysisResults(name);
    return result
        .map(GetAnalysisResponseDto::hasNoPendingAlerts)
        .orElse(Boolean.FALSE);
  }

  private Optional<GetAnalysisResponseDto> safeGetAnalysisResults(String name) {
    try {
      var analysisResult = analysisServiceClient.getAnalysis(name);
      log.info("GetAnalysisRequest, analysis={}, pendingAlerts={}", name, analysisResult.getPendingAlerts());
      return Optional.of(analysisResult);
    } catch (Exception ex) {
      log.error("Exception on get analysis", ex);
      return Optional.empty();
    }
  }
}
