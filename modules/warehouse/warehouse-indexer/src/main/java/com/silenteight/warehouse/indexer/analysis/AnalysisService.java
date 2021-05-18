package com.silenteight.warehouse.indexer.analysis;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.silenteight.warehouse.indexer.analysis.NameResource.getId;

@RequiredArgsConstructor
public class AnalysisService {

  private final AnalysisMetadataRepository analysisMetadataRepository;

  public AnalysisMetadataDto getAnalysisMetadata(
      String analysis, NamingStrategy namingStrategy) {

    return getExistingMetadata(analysis)
        .orElseGet(() -> storeMetadata(analysis, namingStrategy))
        .toDto();
  }

  public String getTenantIdByAnalysis(String analysisName) {
    return analysisMetadataRepository.getByAnalysis(analysisName)
        .map(AnalysisMetadataEntity::getTenant)
        .orElseThrow(() -> new AnalysisDoesNotExistException(
            String.format("Analysis with name %s does not exist.", analysisName)));
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

    return saveEntity(analysisMetadataEntity);
  }

  private AnalysisMetadataEntity saveEntity(AnalysisMetadataEntity analysisMetadataEntity) {
    try {
      return analysisMetadataRepository.save(analysisMetadataEntity);
    } catch (DataIntegrityViolationException e) {
      String analysis = analysisMetadataEntity.getAnalysis();
      return getExistingMetadata(analysis)
          .orElseThrow(() -> new IllegalStateException(
              "Attempt to retrieve analysis metadata failed: analysis=" + analysis));
    }
  }
}
