package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class KibanaIndexFixture {

  public static final String KIBANA_INDEX_ID = "kibana-index-id";
  public static final String ELASTIC_INDEX_PATTERN_VALUE = "alerts*";
  public static final String TIME_FIELD_NAME_VALUE = "indexTimestamp";
  public static final String FIELDS_VALUE =
      "[{\\\"count\\\":0,\\\"name\\\":\\\"_id\\\",\\\"type\\\":\\\"string\\\","
          + "\\\"esTypes\\\":[\\\"_id\\\"],\\\"scripted\\\":false,\\\"searchable\\\":true,"
          + "\\\"aggregatable\\\":true,\\\"readFromDocValues\\\":false}]";

  public static final KibanaIndexPatternAttributes KIBANA_INDEX_PATTERN_ATTRIBUTES =
      KibanaIndexPatternAttributes.builder()
          .title(ELASTIC_INDEX_PATTERN_VALUE)
          .timeFieldName(TIME_FIELD_NAME_VALUE)
          .fields(FIELDS_VALUE)
          .build();
}
