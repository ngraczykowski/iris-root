package com.silenteight.warehouse.report.reasoning.match.generation;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.report.reasoning.match.generation.GenerationAiReasoningMatchLevelReportTestFixtures.PROPERTIES;
import static org.assertj.core.api.Assertions.*;

class AiReasoningMatchLevelReportDefinitionPropertiesTest {

  @Test
  void shouldReturnFieldNames() {
    // when
    List<String> fields = PROPERTIES.getReportFieldsDefinition().getNames();

    // then
    assertThat(fields)
        .hasSize(2)
        .containsExactly(
            AiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_NAME,
            AiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_NAME);
  }

  @Test
  void shouldReturnLabelList() {
    //when
    final List<String> fieldLabels = PROPERTIES.getReportFieldsDefinition().getLabels();

    //then
    assertThat(fieldLabels)
        .hasSize(2)
        .containsExactly(
            AiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_LABEL,
            AiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_LABEL);
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
