package com.silenteight.warehouse.report.billing;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.parse;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class BillingReportTestFixtures {

  public static final long REPORT_ID = 9;
  public static final String FROM_QUERY_PARAM = "2020-01-01";
  public static final String TO_QUERY_PARAM = "2021-10-15";
  public static final LocalDate LOCAL_DATE_FROM = parse(FROM_QUERY_PARAM);
  public static final LocalDate LOCAL_DATE_TO = parse(TO_QUERY_PARAM);
  public static final ReportRange REPORT_RANGE = of(LOCAL_DATE_FROM, LOCAL_DATE_TO);
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String DATE_LABEL = "date";
  public static final String RECOMMENDATION_FIELD_NAME = "alert_s8_recommendation";
  public static final String ALERT_RECOMMENDATION_YEAR = "alert_recommendationYear";
  public static final String ALERT_RECOMMENDATION_MONTH = "alert_recommendationMonth";
  public static final String TEST_INDEX = "test_index";
  public static final List<String> INDEXES = of(TEST_INDEX);
  public static final String ACTION_FALSE_POSITIVE = "ACTION_FALSE_POSITIVE";
  public static final String ACTION_POTENTIAL_TRUE_POSITIVE = "ACTION_POTENTIAL_TRUE_POSITIVE";
  public static final String ACTION_INVESTIGATE = "ACTION_INVESTIGATE";
  public static final String COUNT_ALERTS_SOLVED_FIELD = "count_alerts_solved";
  public static final String COUNT_ALERTS_RECEIVED_FIELD = "count_alerts_received";
  public static final String COUNT_SOLVED_FP = "count_solved_FP";
  public static final String COUNT_SOLVED_PTP = "count_solved_PTP";
  public static final String COUNT_SOLVED_MI = "count_solved_MI";
  public static final String REPORT_FILENAME =
      "Billing_" + FROM_QUERY_PARAM + "_To_" + TO_QUERY_PARAM + ".csv";

  public static final ReportInstanceReferenceDto REPORT_INSTANCE_REFERENCE_DTO =
      new ReportInstanceReferenceDto(REPORT_ID);
}
