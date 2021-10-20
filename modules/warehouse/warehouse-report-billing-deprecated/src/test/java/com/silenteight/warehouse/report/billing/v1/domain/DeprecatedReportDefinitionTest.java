package com.silenteight.warehouse.report.billing.v1.domain;

import lombok.NonNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

class DeprecatedReportDefinitionTest {

  @ParameterizedTest
  @EnumSource(DeprecatedReportDefinition.class)
  void testProperTimeRange(DeprecatedReportDefinition definition) {
    @NonNull OffsetDateTime result = definition.getFrom();
    assertThat(result).isBefore(OffsetDateTime.now());
  }
}
