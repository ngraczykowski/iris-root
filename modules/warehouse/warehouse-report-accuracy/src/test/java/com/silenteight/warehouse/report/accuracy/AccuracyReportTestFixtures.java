package com.silenteight.warehouse.report.accuracy;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;

import static java.util.List.of;

@NoArgsConstructor
public final class AccuracyReportTestFixtures {

  public static final long TIMESTAMP = 1622009305142L;
  public static final long REPORT_ID = 4;
  public static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  public static final String SIMULATION_DEFINITION_ID = "62244c81-7ca6-4334-b896-ee586b254dca";
  public static final String DAY_DEFINITION_ID = "b4219d35-e337-4d1f-af69-9789b325b2a8";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_FILENAME = "accuracy-1-day.csv";
  public static final String SIMULATION_REPORT_TITLE = "Accuracy";
  public static final String SIMULATION_REPORT_DESC = "Simulation Accuracy report";
  public static final String STUB_REPORT_RESPONSE = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String HIT_TYPE_FIELD_NAME = "alert_categories/hitType:value";
  public static final String HIT_TYPE_FIELD_LABEL = "Hit Type";
  public static final String COUNTRY_FIELD_NAME = "s8_country";
  public static final String COUNTRY_FIELD_LABEL = "Country";
  public static final String ANALYST_FIELD_NAME = "alert_analyst_decision";
  public static final String ANALYST_FIELD_POSITIVE_VALUE = "analyst_decision_true_positive";
  public static final String ANALYST_FIELD_NEGATIVE_VALUE = "analyst_decision_false_positive";
  public static final List<String> INDEXES = of("index123");
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);
}
