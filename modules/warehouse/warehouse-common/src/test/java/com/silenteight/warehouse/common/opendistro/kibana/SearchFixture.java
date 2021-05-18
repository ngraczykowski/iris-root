package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.NoArgsConstructor;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexFixture.KIBANA_INDEX_ID;
import static java.util.Collections.emptyList;
import static java.util.Map.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SearchFixture {

  public static final String SEARCH_ID = "search-id";

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
}
