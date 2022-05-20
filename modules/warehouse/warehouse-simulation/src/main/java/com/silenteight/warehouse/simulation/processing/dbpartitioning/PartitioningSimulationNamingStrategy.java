package com.silenteight.warehouse.simulation.processing.dbpartitioning;

import lombok.AllArgsConstructor;

import static java.lang.String.join;

@AllArgsConstructor
public class PartitioningSimulationNamingStrategy {

  private static final String TABLE_NAME = "warehouse_simulation_alert";
  private static final String MATCH_TABLE_NAME = "warehouse_simulation_match";

  public String getPartitionName(String analysisId) {
    return join("_", TABLE_NAME, analysisId.replace("-", "_"));
  }

  public String getMatchPartitionName(String analysisId) {
    return join("_", MATCH_TABLE_NAME, analysisId.replace("-", "_"));
  }
}
