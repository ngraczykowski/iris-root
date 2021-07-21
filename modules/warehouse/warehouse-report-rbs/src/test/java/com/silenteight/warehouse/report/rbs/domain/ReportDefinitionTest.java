package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

class ReportDefinitionTest {

  @ParameterizedTest
  @EnumSource(ReportDefinition.class)
  void testProperTimeRange(ReportDefinition definition) {
    OffsetDateTime result = definition.getFrom(MockTimeSource.ARBITRARY_INSTANCE.now());
    assertThat(result).isBefore(MockTimeSource.ARBITRARY_INSTANCE.offsetDateTime());
  }
}

