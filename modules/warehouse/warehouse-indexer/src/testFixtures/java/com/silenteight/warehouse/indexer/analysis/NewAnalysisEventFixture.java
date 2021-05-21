package com.silenteight.warehouse.indexer.analysis;

import static java.lang.String.join;

public class NewAnalysisEventFixture {

  static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  static final String ANALYSIS = "analysis/" + ANALYSIS_ID;

  public static final String SIMULATION_ENV = "env1";
  public static final String SIMULATION_FLOW = "simulation";
  public static final String SIMULATION_TENANT =
      join("_", SIMULATION_ENV, SIMULATION_FLOW, ANALYSIS_ID);
  public static final String SIMULATION_ELASTIC_SEARCH_INDEX =
      join("_", SIMULATION_ENV, SIMULATION_FLOW, ANALYSIS_ID);
  public static final AnalysisMetadataDto SIMULATION_METADATA = AnalysisMetadataDto.builder()
      .tenant(SIMULATION_TENANT)
      .elasticIndexName(SIMULATION_ELASTIC_SEARCH_INDEX)
      .build();
  public static final NamingStrategy SIMULATION_NAMING_STRATEGY =
      new SimulationNamingStrategy(SIMULATION_ENV);

  public static NewAnalysisEvent createNewSimulationEvent(String analysis) {
    return NewAnalysisEvent.builder()
        .analysis(analysis)
        .simulation(true)
        .analysisMetadataDto(SIMULATION_METADATA)
        .build();
  }

  public static final String PRODUCTION_ENV = "env2";
  public static final String PRODUCTION_FLOW = "production";
  public static final String PRODUCTION_TENANT = join("_", PRODUCTION_ENV, PRODUCTION_FLOW);
  public static final String PRODUCTION_ELASTIC_SEARCH_INDEX =
      join("_", PRODUCTION_ENV, PRODUCTION_FLOW);
  public static final AnalysisMetadataDto PRODUCTION_METADATA = AnalysisMetadataDto.builder()
      .tenant(PRODUCTION_TENANT)
      .elasticIndexName(PRODUCTION_ELASTIC_SEARCH_INDEX)
      .build();
  public static final NamingStrategy PRODUCTION_NAMING_STRATEGY =
      new ProductionNamingStrategy(PRODUCTION_ENV);

  public static NewAnalysisEvent createNewProductionEvent(String analysis) {
    return NewAnalysisEvent.builder()
        .analysis(analysis)
        .simulation(false)
        .analysisMetadataDto(PRODUCTION_METADATA)
        .build();
  }
}
