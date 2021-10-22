package com.silenteight.warehouse.report.reasoning.v1.generation;

import lombok.NoArgsConstructor;

import static com.silenteight.warehouse.report.reasoning.v1.DeprecatedAiReasoningReportTestFixtures.*;
import static java.util.List.of;

@NoArgsConstructor
public final class DeprecatedGenerationAiReasoningReportTestFixtures {

  public static final AiReasoningReportDefinitionProperties PROPERTIES =
      new AiReasoningReportDefinitionProperties(
          DATE_FIELD_NAME,
          of(getColumn(ALERT_STATUS_FIELD_NAME, ALERT_STATUS_FIELD_LABEL),
              getColumn(ALERT_COMMENT_FIELD_NAME, ALERT_COMMENT_FIELD_LABEL)),
          null);

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
