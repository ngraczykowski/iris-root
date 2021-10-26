package com.silenteight.warehouse.report.accuracy.v1.generation;

import lombok.NoArgsConstructor;

import java.util.List;

import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.*;
import static java.util.List.of;

@NoArgsConstructor
public final class DeprecatedGenerationAccuracyReportTestFixtures {

  public static final AccuracyReportDefinitionProperties
      PROPERTIES = new AccuracyReportDefinitionProperties(
      DATE_FIELD_NAME,
      of(
          getColumn(HIT_TYPE_FIELD_NAME, HIT_TYPE_FIELD_LABEL),
          getColumn(COUNTRY_FIELD_NAME, COUNTRY_FIELD_LABEL)),
      of(getFilterProperties(
          ANALYST_FIELD_NAME,
          of(ANALYST_FIELD_POSITIVE_VALUE, ANALYST_FIELD_NEGATIVE_VALUE))
      ));

  public static final AccuracyReportDefinitionProperties NULL_QUERY_FILTERS_PROPERTIES =
      new AccuracyReportDefinitionProperties(
          DATE_FIELD_NAME,
          of(
              getColumn(HIT_TYPE_FIELD_NAME, HIT_TYPE_FIELD_LABEL),
              getColumn(COUNTRY_FIELD_NAME, COUNTRY_FIELD_LABEL)),
          null);

  private static FilterProperties getFilterProperties(String name, List<String> allowedValues) {
    return new FilterProperties(name, allowedValues);
  }

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
