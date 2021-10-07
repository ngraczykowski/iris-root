package com.silenteight.warehouse.indexer.simulation.analysis;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TestAnalysisMetadataRepository extends AnalysisMetadataRepository {

  @Transactional
  @Modifying
  @Query(value = "truncate table warehouse_analysis_metadata", nativeQuery = true)
  void truncateAnalysisMetadata();
}
