package com.silenteight.warehouse.report.metrics.generation;

import lombok.NoArgsConstructor;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.*;
import static java.util.List.of;

@NoArgsConstructor
public final class GenerationMetricsReportTestFixtures {

  public static final PropertiesDefinition PROPERTIES = new PropertiesDefinition(
      ALERT_TIMESTAMP_FIELD,
      getColumn(METRICS_COUNTRY_FIELD, METRICS_COUNTRY_LABEL),
      getColumn(METRICS_RISK_TYPE_FIELD, METRICS_RISK_TYPE_LABEL),
      new GroupingColumnProperties(
          RECOMMENDED_ACTION_FIELD,
          of(FALSE_POSITIVE_VALUE, POTENTIAL_TRUE_POSITIVE_VALUE)));

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
