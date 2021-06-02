package com.silenteight.warehouse.indexer.analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductionNamingStrategy {

  private final String environmentPrefix;

  public String getTenantName() {
    return environmentPrefix + "_production";
  }

  public String getElasticIndexName() {
    return environmentPrefix + "_production";
  }
}
