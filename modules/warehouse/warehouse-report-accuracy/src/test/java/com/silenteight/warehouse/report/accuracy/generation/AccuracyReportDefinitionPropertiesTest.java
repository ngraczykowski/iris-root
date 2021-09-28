package com.silenteight.warehouse.report.accuracy.generation;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.*;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.NULL_QUERY_FILTERS_PROPERTIES;
import static com.silenteight.warehouse.report.accuracy.generation.GenerationAccuracyReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

class AccuracyReportDefinitionPropertiesTest {

  @Test
  void shouldReturnQueryFilters() {
    // when
    List<QueryFilter> filters = PROPERTIES.getQueryFilters();

    // then
    assertThat(filters).hasSize(1);
    QueryFilter queryFilter = filters.get(0);
    assertThat(queryFilter.getField()).isEqualTo(ANALYST_FIELD_NAME);
    assertThat(queryFilter.getAllowedValues())
        .hasSize(2)
        .containsExactly(ANALYST_FIELD_POSITIVE_VALUE, ANALYST_FIELD_NEGATIVE_VALUE);
  }

  @Test
  void shouldReturnFieldNames() {
    // when
    List<String> fields = PROPERTIES.getFieldNames();

    // then
    assertThat(fields)
        .hasSize(2)
        .containsExactly(HIT_TYPE_FIELD_NAME, COUNTRY_FIELD_NAME);
  }

  @Test
  void shouldReturnEmptyQueryFiltersListWhenNull() {
    // when
    List<QueryFilter> filters = NULL_QUERY_FILTERS_PROPERTIES.getQueryFilters();

    // then
    assertThat(filters)
        .isNotNull()
        .isEmpty();
  }
}