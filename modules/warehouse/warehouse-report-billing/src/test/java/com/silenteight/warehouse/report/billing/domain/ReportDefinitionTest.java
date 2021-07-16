package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

class ReportDefinitionTest {

  @ParameterizedTest
  @EnumSource(ReportDefinition.class)
  void testProperTimeRange(ReportDefinition definition) {
    @NonNull OffsetDateTime result = definition.getFrom();
    assertThat(result).isBefore(OffsetDateTime.now());
  }
}
