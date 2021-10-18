package com.silenteight.warehouse.report.reasoning;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;

import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AiReasoningReportTestFixtures {

  public static final long TIMESTAMP = 1622009303142L;
  public static final long REPORT_ID = 2;
  public static final String ANALYSIS_ID = "8ce609a3-660a-4808-9c39-fdf425793070";
  public static final String SIMULATION_DEFINITION_ID = "222c75c8-7261-4444-a4f3-722dc3235201";
  public static final String MONTH_DEFINITION_ID = "1d108cb4-a852-4851-9f20-e3818243dc29";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_FILENAME = "ai-reasoning-30-days.csv";
  public static final String SIMULATION_REPORT_TITLE = "AI Reasoning";
  public static final String SIMULATION_REPORT_DESC = "Simulation AI Reasoning report";
  public static final String STUB_REPORT_RESPONSE = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String ALERT_STATUS_FIELD_NAME = "alert_status";
  public static final String ALERT_STATUS_FIELD_LABEL = "Alert Status";
  public static final String ALERT_COMMENT_FIELD_NAME = "alert_comment";
  public static final String ALERT_COMMENT_FIELD_LABEL = "Alert Comment";
  public static final List<String> INDEXES = of("index444");
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);
}
