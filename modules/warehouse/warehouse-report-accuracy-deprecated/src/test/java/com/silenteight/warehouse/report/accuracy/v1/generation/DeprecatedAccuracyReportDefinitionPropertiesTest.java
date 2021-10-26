package com.silenteight.warehouse.report.accuracy.v1.generation;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.*;
import static com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedGenerationAccuracyReportTestFixtures.NULL_QUERY_FILTERS_PROPERTIES;
import static com.silenteight.warehouse.report.accuracy.v1.generation.DeprecatedGenerationAccuracyReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

class DeprecatedAccuracyReportDefinitionPropertiesTest {

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
    List<String> fields = PROPERTIES.getReportFieldsDefinition().getNames();

    // then
    assertThat(fields)
        .hasSize(2)
        .containsExactly(HIT_TYPE_FIELD_NAME, COUNTRY_FIELD_NAME);
  }

  @Test
  void shouldReturnFieldLabels() {
    // when
    List<String> labels = PROPERTIES.getReportFieldsDefinition().getLabels();

    // then
    assertThat(labels)
        .hasSize(2)
        .containsExactly(HIT_TYPE_FIELD_LABEL, COUNTRY_FIELD_LABEL);
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
