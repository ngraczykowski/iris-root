package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchTestConstants {

  // The tenant that is created by default in opendistro images.
  public static final String TENANT = "admin_tenant";

  // Elasticsearch index that is referenced by kibana index
  // created via '1-create-kibana-index.json'
  public static final String INDEX_NAME = "alerts";
}
