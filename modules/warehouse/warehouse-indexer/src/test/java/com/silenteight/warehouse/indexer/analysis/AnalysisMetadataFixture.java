package com.silenteight.warehouse.indexer.analysis;

import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;

public class AnalysisMetadataFixture {

  static final String ELASTIC_INDEX_PATTERN = "env_simulation_" + ANALYSIS_ID;
  static final String TENANT = "env_simulation_" + ANALYSIS_ID;

  static final AnalysisMetadataEntity ANALYSIS_METADATA = AnalysisMetadataEntity.builder()
      .analysis(ANALYSIS)
      .analysisId(ANALYSIS_ID)
      .elasticIndexPattern(ELASTIC_INDEX_PATTERN)
      .tenant(TENANT)
      .build();
}
