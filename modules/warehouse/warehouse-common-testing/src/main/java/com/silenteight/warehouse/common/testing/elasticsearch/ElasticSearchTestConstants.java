package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchTestConstants {

  // The tenants that are created by default in opendistro images.
  public static final String ADMIN_TENANT = "admin_tenant";

  // Elasticsearch index that is referenced via pattern by kibana index
  // created via '1-create-kibana-index.json'
  public static final String SIMULATION_ANALYSIS_ID = "9630b08f-682c-4565-bf4d-c07064c65615";
  public static final String SIMULATION_ANALYSIS = "analysis/" + SIMULATION_ANALYSIS_ID;
  public static final String INDEX_NAME_PATTERN = "itest_simulation_*";
  public static final String ELASTIC_INDEX_NAME = "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  // Kibana index-pattern that is created via
  // '1-create-kibana-index.json` and referenced in '2-create-saved-search.json'
  public static final String KIBANA_INDEX_PATTERN_NAME = "all_itest_simulation";

  // Saved search id that is created via
  // '2-create-saved-search.json' and referenced in '3-create-report-definition.json'
  public static final String SAVED_SEARCH = "ai-resoning-wl-search-id";
}
