package com.silenteight.warehouse.report.reasoning.match.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures.*;
import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenerationAiReasoningMatchLevelReportTestFixtures {

  public static final AiReasoningMatchLevelReportDefinitionProperties PROPERTIES =
      new AiReasoningMatchLevelReportDefinitionProperties(
          DATE_FIELD_NAME,
          of(
              getColumn(ALERT_STATUS_FIELD_NAME, ALERT_STATUS_FIELD_LABEL),
              getColumn(ALERT_COMMENT_FIELD_NAME, ALERT_COMMENT_FIELD_LABEL)),
          null);

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
