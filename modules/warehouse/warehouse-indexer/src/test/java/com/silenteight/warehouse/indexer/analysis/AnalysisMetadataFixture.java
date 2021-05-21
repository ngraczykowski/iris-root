package com.silenteight.warehouse.indexer.analysis;

public class AnalysisMetadataFixture {

  static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  static final String ANALYSIS = "analysis/" + ANALYSIS_ID;
  static final String ELASTIC_INDEX_PATTERN = "env_simulation_" + ANALYSIS_ID;
  static final String TENANT = "env_simulation_" + ANALYSIS_ID;

  static final AnalysisMetadataEntity ANALYSIS_METADATA = AnalysisMetadataEntity.builder()
      .analysis(ANALYSIS)
      .analysisId(ANALYSIS_ID)
      .elasticIndexPattern(ELASTIC_INDEX_PATTERN)
      .tenant(TENANT)
      .build();
}
