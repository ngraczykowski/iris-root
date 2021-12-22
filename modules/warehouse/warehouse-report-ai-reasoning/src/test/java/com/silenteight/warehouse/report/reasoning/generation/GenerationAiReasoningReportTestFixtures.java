package com.silenteight.warehouse.report.reasoning.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.*;
import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenerationAiReasoningReportTestFixtures {

  public static final AiReasoningReportDefinitionProperties PROPERTIES =
      new AiReasoningReportDefinitionProperties(
          DATE_FIELD_NAME,
          false,
          null,
          null,
          of(
              getColumn(ALERT_STATUS_FIELD_NAME, ALERT_STATUS_FIELD_LABEL),
              getColumn(ALERT_COMMENT_FIELD_NAME, ALERT_COMMENT_FIELD_LABEL)),
          null);

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
