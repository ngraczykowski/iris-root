package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.AllArgsConstructor;

import static com.silenteight.warehouse.indexer.simulation.analysis.NameResource.getId;

@AllArgsConstructor
public class SimulationNamingStrategy {

  private final String environmentPrefix;
  private static final String SIMULATION = "_simulation_";

  public String getTenantName(String analysisId) {

    return environmentPrefix + SIMULATION + analysisId;
  }

  public String getElasticIndexName(String analysisId) {
    return environmentPrefix + SIMULATION + analysisId;
  }

  public String getElasticIndexNameForAnalysisResource(String analysisName) {
    return environmentPrefix + SIMULATION + getId(analysisName);
  }
}
