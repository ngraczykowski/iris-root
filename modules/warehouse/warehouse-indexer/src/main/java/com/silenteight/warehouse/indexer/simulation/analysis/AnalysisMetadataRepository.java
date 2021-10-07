package com.silenteight.warehouse.indexer.simulation.analysis;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface AnalysisMetadataRepository extends Repository<AnalysisMetadataEntity, Long> {

  AnalysisMetadataEntity save(AnalysisMetadataEntity analysisMetadataEntity);

  Optional<AnalysisMetadataEntity> getByAnalysis(String analysis);

  List<AnalysisMetadataEntity> findAll();
}
