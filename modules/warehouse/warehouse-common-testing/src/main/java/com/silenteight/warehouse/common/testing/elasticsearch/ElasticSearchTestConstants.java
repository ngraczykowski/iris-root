package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchTestConstants {

  // The tenants that are created by default in opendistro images.
  public static final String ADMIN_TENANT = "admin_tenant";
  public static final String OTHER_TENANT = "global_tenant";

  // Elasticsearch index that is referenced by kibana index
  // created via '1-create-kibana-index.json'
  public static final String INDEX_NAME = "alerts";

  // Kibana index-pattern that is created via
  // '1-create-kibana-index.json`
  public static final String KIBANA_INDEX_PATTERN_NAME = "alerts-index";
}
