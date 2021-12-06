package com.silenteight.warehouse.report.metrics.v1.generation;

import lombok.NoArgsConstructor;

import static com.silenteight.warehouse.report.metrics.v1.DeprecatedMetricsReportTestFixtures.*;
import static java.util.Arrays.asList;
import static java.util.List.of;

@NoArgsConstructor
public final class DeprecatedGenerationMetricsReportTestFixtures {

  public static final PropertiesDefinition PROPERTIES = new PropertiesDefinition(
      getGroupingColumn(DATE_FIELD_NAME, DATE_FIELD_LABEL, DATE_OLD_PATTERN, DATE_NEW_PATTERN),
      getGroupingColumn(COUNTRY_FIELD_NAME, COUNTRY_FIELD_LABEL, null, null),
      getGroupingColumn(RISK_TYPE_FIELD_NAME, RISK_TYPE_FIELD_LABEL, null, null),
      getGroupingColumn(HIT_TYPE_FIELD_NAME, HIT_TYPE_FIELD_LABEL, null, null),
      getColumn(
          RECOMMENDATION_FIELD_NAME,
          RECOMMENDATION_FIELD_POSITIVE_VALUE,
          RECOMMENDATION_FIELD_NEGATIVE_VALUE),
      getColumn(
          ANALYST_FIELD_NAME,
          ANALYST_FIELD_POSITIVE_VALUE,
          ANALYST_FIELD_NEGATIVE_VALUE
      ),
      getColumn(
          QA_FIELD_NAME,
          QA_FIELD_POSITIVE_VALUE,
          QA_FIELD_NEGATIVE_VALUE
      ),
      of(getFilter(ALERT_STATUS_FIELD, COMPLETED_VALUE)),
      getLabel(EFFICIENCY_LABEL),
      getLabel(PTP_EFFICIENCY_LABEL),
      getLabel(FP_EFFICIENCY_LABEL));

  private static GroupingColumnProperties getGroupingColumn(
      String name, String label, String oldPattern, String newPattern) {

    return new GroupingColumnProperties(name, label, oldPattern, newPattern);
  }

  private static ColumnProperties getColumn(
      String name, String positiveValue, String negativeValue) {

    return new ColumnProperties(name, positiveValue, negativeValue);
  }

  private static FilterProperties getFilter(String name, String... values) {
    return new FilterProperties(name, asList(values));
  }

  private static LabelProperties getLabel(String name) {
    return new LabelProperties(name);
  }
}
