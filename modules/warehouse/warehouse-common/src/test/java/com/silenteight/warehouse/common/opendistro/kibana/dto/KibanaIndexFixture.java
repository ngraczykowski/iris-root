package com.silenteight.warehouse.common.opendistro.kibana.dto;

import java.util.Map;

import static java.util.Map.of;

public class KibanaIndexFixture {

  public static final String SAVED_OBJECT_ID = "123";

  public static final String ELASTIC_INDEX_PATTERN_KEY = "title";
  public static final String ELASTIC_INDEX_PATTERN_VALUE = "alerts*";
  public static final String TIME_FIELD_NAME_KEY = "timeFieldName";
  public static final String TIME_FIELD_NAME_VALUE = "indexTimestamp";

  public static final Map<String, String> ATTRIBUTES = of(
      ELASTIC_INDEX_PATTERN_KEY, ELASTIC_INDEX_PATTERN_VALUE,
      TIME_FIELD_NAME_KEY, TIME_FIELD_NAME_VALUE
  );
}
