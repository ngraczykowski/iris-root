package com.silenteight.warehouse.report.metrics;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.parse;
import static java.util.List.of;

@NoArgsConstructor
public final class MetricsReportTestFixtures {

  public static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  public static final String FROM_QUERY_PARAM = "2021-08-15";
  public static final String TO_QUERY_PARAM = "2021-10-15";
  public static final String REPORT_FILENAME =
      "Production_Metrics_" + FROM_QUERY_PARAM + "_To_" + TO_QUERY_PARAM + ".csv";

  public static final String REPORT_CONTENT = "content";
  public static final long REPORT_ID = 4;
  public static final String DATE = "2021-07-31 23:59:59.999";
  public static final String DATE_2 = "2021-06-31 23:59:59.999";
  public static final ReportDto REPORT_DTO = ReportDto.of(REPORT_FILENAME, REPORT_CONTENT);
  public static final ReportInstanceReferenceDto REPORT_INSTANCE_REFERENCE_DTO =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String DATE_FIELD_LABEL = "Date";
  public static final String DATE_OLD_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_NEW_PATTERN = "yyyy-MM";
  public static final String COUNTRY_FIELD_NAME = "s8_country";
  public static final String COUNTRY_FIELD_LABEL = "Country LoB";
  public static final String RISK_TYPE_FIELD_NAME = "alert_categories/hitType";
  public static final String RISK_TYPE_FIELD_LABEL = "Risk Type";
  public static final String RECOMMENDATION_FIELD_NAME = "alert_s8_recommendation";
  public static final String RECOMMENDATION_FIELD_POSITIVE_VALUE = "ACTION_POTENTIAL_TRUE_POSITIVE";
  public static final String RECOMMENDATION_FIELD_NEGATIVE_VALUE = "ACTION_FALSE_POSITIVE";
  public static final String RECOMMENDATION_FIELD_MEANINGLESS_VALUE = "ACTION_MANUAL_INVESTIGATE";
  public static final String ANALYST_FIELD_NAME = "alert_analyst_decision";
  public static final String ANALYST_FIELD_POSITIVE_VALUE = "analyst_decision_true_positive";
  public static final String ANALYST_FIELD_NEGATIVE_VALUE = "analyst_decision_false_positive";
  public static final String QA_FIELD_NAME = "alert_qa.level-0.state";
  public static final String QA_FIELD_POSITIVE_VALUE = "qa_decision_PASSED";
  public static final String QA_FIELD_NEGATIVE_VALUE = "qa_decision_FAILED";
  public static final List<String> INDEXES = of("index123");
  public static final String COUNTRY_DE = "DE";
  public static final String RISK_TYPE_FRAUD = "Fraud";
  public static final String HIT_TYPE_FIELD_NAME = "alert_DN_CASE.ExtendedAttribute5";
  public static final String HIT_TYPE_FIELD_LABEL = "List Type";
  public static final String ALERT_STATUS_FIELD = "alert_status";
  public static final String COMPLETED_VALUE = "COMPLETED";
  public static final LocalDate LOCAL_DATE_FROM = parse(FROM_QUERY_PARAM);
  public static final LocalDate LOCAL_DATE_TO = parse(TO_QUERY_PARAM);
  public static final ReportRange REPORT_RANGE = of(LOCAL_DATE_FROM, LOCAL_DATE_TO);
}
