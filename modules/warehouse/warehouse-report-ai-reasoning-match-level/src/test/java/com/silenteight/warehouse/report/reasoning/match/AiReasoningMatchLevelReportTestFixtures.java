package com.silenteight.warehouse.report.reasoning.match;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;

import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AiReasoningMatchLevelReportTestFixtures {

  public static final long TIMESTAMP = 162449303142L;
  public static final long REPORT_ID = 32;
  public static final String ANALYSIS_ID = "386028d5-811d-48fd-9b34-56c6b10c48c6";
  public static final String SIMULATION_DEFINITION_ID = "92caf78a-723f-42b1-bde1-21b77e7a5c21";
  public static final String MONTH_DEFINITION_ID = "daa38c15-e811-412b-ac31-d7053fdc319d";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_FILENAME = "ai-reasoning-match-level-30-days.csv";
  public static final String STUB_REPORT_RESPONSE = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String ALERT_STATUS_FIELD_NAME = "alert_status";
  public static final String ALERT_STATUS_FIELD_LABEL = "Alert Status";
  public static final String ALERT_COMMENT_FIELD_NAME = "alert_comment";
  public static final String ALERT_COMMENT_FIELD_LABEL = "Alert Comment";
  public static final List<String> INDEXES = of("index455");
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);
}
