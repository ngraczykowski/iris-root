package com.silenteight.warehouse.report.reasoning.match.generation;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures;

import static java.util.List.of;

@NoArgsConstructor
public final class GenerationAiReasoningMatchLevelReportTestFixtures {

  public static final AiReasoningReportDefinitionProperties PROPERTIES =
      new AiReasoningReportDefinitionProperties(
          AiReasoningMatchLevelReportTestFixtures.DATE_FIELD_NAME,
          of(
              getColumn(
                  AiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_NAME,
                  AiReasoningMatchLevelReportTestFixtures.ALERT_STATUS_FIELD_LABEL),
              getColumn(
                  AiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_NAME,
                  AiReasoningMatchLevelReportTestFixtures.ALERT_COMMENT_FIELD_LABEL)),
          null);

  private static ColumnProperties getColumn(String name, String label) {
    return new ColumnProperties(name, label);
  }
}
