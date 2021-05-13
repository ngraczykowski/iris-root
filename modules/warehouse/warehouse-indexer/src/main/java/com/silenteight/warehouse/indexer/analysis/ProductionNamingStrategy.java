package com.silenteight.warehouse.indexer.analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductionNamingStrategy implements NamingStrategy {

  private final String environmentPrefix;

  @Override
  public String getTenantName(String analysisId) {
    return environmentPrefix + "_production";
  }

  @Override
  public String getElasticIndexName(String analysisId) {
    return environmentPrefix + "_production";
  }
}
