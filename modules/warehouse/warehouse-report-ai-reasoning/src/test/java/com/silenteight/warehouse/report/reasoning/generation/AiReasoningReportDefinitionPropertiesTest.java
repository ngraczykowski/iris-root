package com.silenteight.warehouse.report.reasoning.generation;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.ALERT_COMMENT_FIELD_NAME;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.ALERT_STATUS_FIELD_NAME;
import static com.silenteight.warehouse.report.reasoning.generation.GenerationAiReasoningReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

class AiReasoningReportDefinitionPropertiesTest {

  @Test
  void shouldReturnFieldNames() {
    // when
    List<String> fields = PROPERTIES.getFieldNames();

    // then
    assertThat(fields)
        .hasSize(2)
        .containsExactly(ALERT_STATUS_FIELD_NAME, ALERT_COMMENT_FIELD_NAME);
  }

  @Test
  void shouldReturnEmptyQueryFiltersListWhenNull() {
    // when
    List<QueryFilter> filters = PROPERTIES.getQueryFilters();

    // then
    assertThat(filters)
        .isNotNull()
        .isEmpty();
  }
}