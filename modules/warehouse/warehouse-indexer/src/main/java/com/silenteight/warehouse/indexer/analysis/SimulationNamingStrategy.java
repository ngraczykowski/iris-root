package com.silenteight.warehouse.indexer.analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimulationNamingStrategy implements NamingStrategy {

  private final String environmentPrefix;

  @Override
  public String getTenantName(String analysisId) {
    return environmentPrefix + "_simulation_" + analysisId;
  }

  @Override
  public String getElasticIndexName(String analysisId) {
    return environmentPrefix + "_simulation_" + analysisId;
  }
}
