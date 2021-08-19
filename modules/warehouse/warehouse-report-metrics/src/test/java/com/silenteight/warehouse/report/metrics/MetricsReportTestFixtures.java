package com.silenteight.warehouse.report.metrics;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;

import static java.util.List.of;

@NoArgsConstructor
public final class MetricsReportTestFixtures {

  public static final long TIMESTAMP = 1622009305142L;
  public static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  public static final String SIMULATION_DEFINITION_ID = "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e";
  public static final String DAY_DEFINITION_ID = "da752407-6047-423a-b7d6-45a72462cec8";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_FILENAME = "metrics-1-day.csv";
  public static final String REPORT_CONTENT = "content";
  public static final String METRICS_COUNTRY_FIELD = "country";
  public static final String METRICS_COUNTRY_LABEL = "Country";
  public static final String METRICS_RISK_TYPE_LABEL = "Risk Type";
  public static final String METRICS_RISK_TYPE_FIELD = "riskType";
  public static final String ALERT_TIMESTAMP_FIELD = "alert_timestamp";
  public static final String RECOMMENDED_ACTION_FIELD = "recommendation-field";
  public static final String FALSE_POSITIVE_VALUE = "ACTION_FALSE_POSITIVE";
  public static final String POTENTIAL_TRUE_POSITIVE_VALUE = "ACTION_POTENTIAL_TRUE_POSITIVE";
  public static final List<String> INDEXES = of("local_production");
  public static final long REPORT_ID = 4;
  public static final ReportDto REPORT_DTO = ReportDto.of(REPORT_FILENAME, REPORT_CONTENT);
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);
}
