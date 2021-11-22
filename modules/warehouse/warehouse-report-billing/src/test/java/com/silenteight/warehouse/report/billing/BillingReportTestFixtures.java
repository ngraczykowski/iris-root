package com.silenteight.warehouse.report.billing;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.billing.domain.dto.BillingReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.parse;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class BillingReportTestFixtures {

  public static final long REPORT_ID = 9;
  public static final String QUERY_PARAM_FROM = "2020-01-01";
  public static final String QUERY_PARAM_TO = "2021-10-15";
  public static final LocalDate LOCAL_DATE_FROM = parse(QUERY_PARAM_FROM);
  public static final LocalDate LOCAL_DATE_TO = parse(QUERY_PARAM_TO);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM = of(LOCAL_DATE_FROM, MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_TO = of(LOCAL_DATE_TO, MIDNIGHT, UTC);
  public static final ReportRange REPORT_RANGE = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);
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
  public static final String CREATION_TIMESTAMP = "1616574866666";
  public static final String CONTENT = "report content";
  public static final String REPORT_FILENAME =
      "Billing_Prod_" + LOCAL_DATE_FROM + "_To_" + QUERY_PARAM_TO + ".csv";

  public static final ReportInstanceReferenceDto REPORT_INSTANCE_REFERENCE_DTO =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final BillingReportDto BILLING_REPORT_DTO =
      BillingReportDto.builder()
          .range(REPORT_RANGE)
          .content(CONTENT)
          .timestamp(CREATION_TIMESTAMP)
          .build();
}
