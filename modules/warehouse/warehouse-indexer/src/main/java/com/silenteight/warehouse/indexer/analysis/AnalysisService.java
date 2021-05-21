package com.silenteight.warehouse.indexer.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.silenteight.warehouse.indexer.analysis.NameResource.getId;
import static java.lang.String.format;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@RequiredArgsConstructor
public class AnalysisService {

  @NonNull
  private final AnalysisMetadataRepository analysisMetadataRepository;

  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  public Optional<AnalysisMetadataDto> getAnalysisMetadata(String analysis) {
    return getExistingMetadata(analysis).map(AnalysisMetadataEntity::toDto);
  }

  @Transactional(propagation = REQUIRES_NEW)
  public AnalysisMetadataDto createAnalysisMetadata(
      String analysis, NamingStrategy namingStrategy) {

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

  private AnalysisMetadataEntity storeMetadata(String analysisName, NamingStrategy namingStrategy) {
    String analysisId = getId(analysisName);

    String elasticIndexName = namingStrategy.getElasticIndexName(analysisId);
    String tenantName = namingStrategy.getTenantName(analysisId);

    AnalysisMetadataEntity analysisMetadataEntity = AnalysisMetadataEntity.builder()
        .analysisId(analysisId)
        .analysis(analysisName)
        .tenant(tenantName)
        .elasticIndexPattern(elasticIndexName)
        .build();

    NewAnalysisEvent newAnalysisEvent = NewAnalysisEvent.builder()
        .analysis(analysisName)
        .simulation(namingStrategy instanceof SimulationNamingStrategy)
        .analysisMetadataDto(analysisMetadataEntity.toDto())
        .build();
    eventPublisher.publishEvent(newAnalysisEvent);

    return analysisMetadataRepository.save(analysisMetadataEntity);
  }
}
