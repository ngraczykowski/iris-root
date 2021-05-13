package com.silenteight.warehouse.indexer.analysis;

public interface NamingStrategy {

  String getTenantName(String analysisId);

  String getElasticIndexName(String analysisId);
}
