package com.silenteight.warehouse.report.reasoning.match.v1.generation;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_LABEL;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_NAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_LABEL;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_NAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.generation.DeprecatedGenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

class DeprecatedAiReasoningMatchLevelReportDefinitionPropertiesTest {

  @Test
  void shouldReturnFieldNames() {
    // when
    List<String> fields = PROPERTIES.getReportFieldsDefinition().getNames();

    // then
    assertThat(fields)
        .hasSize(2)
        .containsExactly(ALERT_STATUS_FIELD_NAME, ALERT_COMMENT_FIELD_NAME);
  }

  @Test
  void shouldReturnLabelList() {
    //when
    final List<String> fieldLabels = PROPERTIES.getReportFieldsDefinition().getLabels();

    //then
    assertThat(fieldLabels)
        .hasSize(2)
        .containsExactly(ALERT_STATUS_FIELD_LABEL, ALERT_COMMENT_FIELD_LABEL);
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
