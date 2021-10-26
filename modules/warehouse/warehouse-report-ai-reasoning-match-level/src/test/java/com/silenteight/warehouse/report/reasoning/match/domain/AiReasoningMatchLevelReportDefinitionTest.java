package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportTypeNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.getReportType;
import static org.assertj.core.api.Assertions.*;

class AiReasoningMatchLevelReportDefinitionTest {

  @ParameterizedTest
  @EnumSource(AiReasoningMatchLevelReportDefinition.class)
  void testProperTimeRange(AiReasoningMatchLevelReportDefinition definition) {
    OffsetDateTime result = definition.getFrom(MockTimeSource.ARBITRARY_INSTANCE.now());
    assertThat(result).isBefore(MockTimeSource.ARBITRARY_INSTANCE.offsetDateTime());
  }

  @Test
  void shouldThrowReportTypeNotFoundExceptionWhenInvalidId() {
    // given
    String invalidId = "c50bd173-a22c-41c8-83dc-a99f8becad7e";

    // when + then
    assertThatThrownBy(() -> getReportType(invalidId))
        .isInstanceOf(ReportTypeNotFoundException.class)
        .hasMessageContaining(invalidId);
  }
}
