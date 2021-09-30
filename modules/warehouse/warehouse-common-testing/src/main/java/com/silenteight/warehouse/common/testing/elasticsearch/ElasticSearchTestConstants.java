package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchTestConstants {

  // The tenants that are created by default in opendistro images.
  public static final String ADMIN_TENANT = "admin_tenant";

  // Kibana index-pattern that is created via 'simulation/1-create-kibana-index.json`
  // and referenced in 'simulation/2-create-saved-search.json'
  public static final String SIMULATION_KIBANA_INDEX_PATTERN_NAME = "all_simulation";

  // Elasticsearch index that is referenced via pattern by kibana index
  // created via 'production/1-create-kibana-index.json'
  public static final String PRODUCTION_ELASTIC_INDEX_NAME = "itest_production";

  // Kibana index-pattern that is created via 'production/1-create-kibana-index.json`
  // and referenced in 'production/2-create-saved-search.json'
  public static final String PRODUCTION_KIBANA_INDEX_PATTERN_NAME = "all_production";

  // Saved search id that is created via
  // '2-create-saved-search.json' and referenced in '3-create-report-definition.json'
  public static final String SAVED_SEARCH = "ai-resoning-wl-search";
}
