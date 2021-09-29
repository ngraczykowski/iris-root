package com.silenteight.warehouse.indexer.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UniqueAnalysisFactory {

  @NonNull
  private final SimulationAnalysisService simulationAnalysisService;

  @NonNull
  private final SimulationNamingStrategy simulationNamingStrategy;

  public AnalysisMetadataDto getUniqueAnalysis(String analysis) {

    Optional<AnalysisMetadataDto> analysisMetadata =
        simulationAnalysisService.getAnalysisMetadata(analysis);
    if (analysisMetadata.isPresent())
      return analysisMetadata.get();

    try {
      return simulationAnalysisService.createAnalysisMetadata(analysis, simulationNamingStrategy);
    } catch (DataIntegrityViolationException e) {
      log.debug("Attempt to create analysis metadata failed. Reattempting to fetch from db.", e);
    }

    return simulationAnalysisService.getAnalysisMetadata(analysis)
        .orElseThrow(() -> new IllegalStateException(
            "Attempt to fetch analysis metadata failed: analysis=" + analysis));
  }
}
