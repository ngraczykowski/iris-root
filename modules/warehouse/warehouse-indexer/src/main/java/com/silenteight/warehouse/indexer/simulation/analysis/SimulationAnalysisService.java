package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.silenteight.warehouse.indexer.simulation.analysis.NameResource.getId;
import static java.lang.String.format;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@RequiredArgsConstructor
public class SimulationAnalysisService {

  @NonNull
  private final AnalysisMetadataRepository analysisMetadataRepository;

  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  public Optional<AnalysisMetadataDto> getAnalysisMetadata(String analysis) {
    return getExistingMetadata(analysis).map(AnalysisMetadataEntity::toDto);
  }

  @Transactional(propagation = REQUIRES_NEW)
  public AnalysisMetadataDto createAnalysisMetadata(
      String analysis, SimulationNamingStrategy namingStrategy) {

    return storeMetadata(analysis, namingStrategy).toDto();
  }

  public String getTenantIdByAnalysis(String analysisName) {
    return analysisMetadataRepository.getByAnalysis(analysisName)
        .map(AnalysisMetadataEntity::getTenant)
        .orElseThrow(() -> new AnalysisDoesNotExistException(
            format("Analysis with name %s does not exist.", analysisName)));
  }

  private Optional<AnalysisMetadataEntity> getExistingMetadata(String analysis) {
    return analysisMetadataRepository.getByAnalysis(analysis);
  }

  private AnalysisMetadataEntity storeMetadata(
      String analysisName, SimulationNamingStrategy simulationNamingStrategy) {

    String analysisId = getId(analysisName);

    String elasticIndexName = simulationNamingStrategy.getElasticIndexName(analysisId);
    String tenantName = simulationNamingStrategy.getTenantName(analysisId);

    AnalysisMetadataEntity analysisMetadataEntity = AnalysisMetadataEntity.builder()
        .analysisId(analysisId)
        .analysis(analysisName)
        .tenant(tenantName)
        .elasticIndexPattern(elasticIndexName)
        .build();

    NewSimulationAnalysisEvent newSimulationAnalysisEvent = NewSimulationAnalysisEvent.builder()
        .analysis(analysisName)
        .analysisMetadataDto(analysisMetadataEntity.toDto())
        .build();
    eventPublisher.publishEvent(newSimulationAnalysisEvent);

    return analysisMetadataRepository.save(analysisMetadataEntity);
  }
}
