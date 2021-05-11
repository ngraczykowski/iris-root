package com.silenteight.warehouse.common.opendistro.kibana;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Map.of;

public class KibanaIndexFixture {

  public static final String KIBANA_INDEX_ID = "kibana-index-id";
  public static final String SEARCH_ID = "search-id";
  public static final String ELASTIC_INDEX_PATTERN_VALUE = "alerts*";
  public static final String TIME_FIELD_NAME_VALUE = "indexTimestamp";
  public static final String FIELDS_VALUE =
      "[{\\\"count\\\":0,\\\"name\\\":\\\"_id\\\",\\\"type\\\":\\\"string\\\","
          + "\\\"esTypes\\\":[\\\"_id\\\"],\\\"scripted\\\":false,\\\"searchable\\\":true,"
          + "\\\"aggregatable\\\":true,\\\"readFromDocValues\\\":false}]";

  public static final String OBJECT_META =
      "{\\\"highlightAll\\\":true,\\\"version\\\":true,\\\"query\\\":{\\\"query\\\":\\\"\\\","
          + "\\\"language\\\":\\\"kuery\\\"},\\\"filter\\\":[],"
          + "\\\"indexRefName\\\":\\\"kibanaSavedObjectMeta.searchSourceJSON.index\\\"}";

  public static final List<String> SEARCH_ATTRIBUTES_COLUMNS = List.of(
      "alert.recommendation",
      "alert.name");

  public static final SearchAttributes SAVED_SEARCH_ATTRIBUTES =
      SearchAttributes.builder()
          .title("basic-search")
          .description("basic-description")
          .columns(SEARCH_ATTRIBUTES_COLUMNS)
          .sort(emptyList())
          .kibanaSavedObjectMeta(of("searchSourceJSON", OBJECT_META))
          .build();

  public static final SavedObjectReference SAVED_SEARCH_REFERENCES = SavedObjectReference.builder()
      .name("kibanaSavedObjectMeta.searchSourceJSON.index")
      .type("index-pattern")
      .id(KIBANA_INDEX_ID)
      .build();

  public static final KibanaIndexPatternAttributes KIBANA_INDEX_PATTERN_ATTRIBUTES =
      KibanaIndexPatternAttributes.builder()
          .title(ELASTIC_INDEX_PATTERN_VALUE)
          .timeFieldName(TIME_FIELD_NAME_VALUE)
          .fields(FIELDS_VALUE)
          .build();
}
