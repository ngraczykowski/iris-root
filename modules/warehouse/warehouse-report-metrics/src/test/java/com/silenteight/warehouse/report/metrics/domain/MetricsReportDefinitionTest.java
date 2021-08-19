package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.report.metrics.domain.exception.ReportTypeNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.metrics.domain.MetricsReportDefinition.getReportType;
import static org.assertj.core.api.Assertions.*;

class MetricsReportDefinitionTest {

  @ParameterizedTest
  @EnumSource(MetricsReportDefinition.class)
  void testProperTimeRange(MetricsReportDefinition definition) {
    OffsetDateTime result = definition.getFrom(MockTimeSource.ARBITRARY_INSTANCE.now());
    assertThat(result).isBefore(MockTimeSource.ARBITRARY_INSTANCE.offsetDateTime());
  }

  @Test
  void shouldThrowReportTypeNotFoundExceptionWhenInvalidId() {
    // given
    String invalidId = "d0032304-18de-4654-acb9-00d3f73bea52";

    // when + then
    assertThatThrownBy(() -> getReportType(invalidId))
        .isInstanceOf(ReportTypeNotFoundException.class)
        .hasMessageContaining(invalidId);
  }
}
