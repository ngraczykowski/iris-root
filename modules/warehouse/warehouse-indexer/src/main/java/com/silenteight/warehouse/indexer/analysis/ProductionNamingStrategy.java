package com.silenteight.warehouse.indexer.analysis;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductionNamingStrategy {

  private final String environmentPrefix;

  public String getElasticWriteIndexName() {
    return environmentPrefix + "_production.old";
  }
}
