package com.silenteight.warehouse.indexer.analysis;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.ANALYSIS_RESOURCE_PREFIX;
import static java.lang.String.join;

public class NewAnalysisEventFixture {

  public static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  public static final String ANALYSIS = ANALYSIS_RESOURCE_PREFIX + ANALYSIS_ID;

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
  public static final SimulationNamingStrategy SIMULATION_NAMING_STRATEGY =
      new SimulationNamingStrategy(SIMULATION_ENV);

  public static NewSimulationAnalysisEvent createNewSimulationEvent(String analysis) {
    return NewSimulationAnalysisEvent.builder()
        .analysis(analysis)
        .analysisMetadataDto(SIMULATION_METADATA)
        .build();
  }
}
