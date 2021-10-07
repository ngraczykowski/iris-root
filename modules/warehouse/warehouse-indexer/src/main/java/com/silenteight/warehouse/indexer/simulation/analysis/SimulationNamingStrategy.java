package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimulationNamingStrategy {

  private final String environmentPrefix;

  public String getTenantName(String analysisId) {
    return environmentPrefix + "_simulation_" + analysisId;
  }

  public String getElasticIndexName(String analysisId) {
    return environmentPrefix + "_simulation_" + analysisId;
  }
}
