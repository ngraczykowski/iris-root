package com.silenteight.warehouse.indexer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class IndexerFixtures {

  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";
  public static final String SIMULATION_ELASTIC_INDEX_NAME =
      "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  public static final String PRODUCTION_ELASTIC_WRITE_INDEX_NAME = "itest_production.2021-04-15";
  public static final String PRODUCTION_ELASTIC_READ_ALIAS_NAME = "itest_production";
}
