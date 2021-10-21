package com.silenteight.warehouse.report.rbs;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.parse;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RbsReportTestFixtures {

  public static final long REPORT_ID = 7;
  public static final String ANALYSIS_ID = "123";
  public static final String FROM_QUERY_PARAM = "2021-08-15";
  public static final String TO_QUERY_PARAM = "2021-10-15";
  public static final LocalDate LOCAL_DATE_FROM = parse(FROM_QUERY_PARAM);
  public static final LocalDate LOCAL_DATE_TO = parse(TO_QUERY_PARAM);
  public static final ReportRange REPORT_RANGE = of(LOCAL_DATE_FROM, LOCAL_DATE_TO);
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String POLICY_ID_FIELD_NAME = "alert_policyId";
  public static final String POLICY_ID_FIELD_LABEL = "policy_id";
  public static final String RECOMMENDATION_FIELD_NAME = "alert_s8_recommendation";
  public static final String RECOMMENDATION_FIELD_LABEL = "alert_recommendation";
  public static final String TEST_INDEX = "test_index";
  public static final List<String> INDEXES = List.of(TEST_INDEX);
  public static final String REPORT_FILENAME =
      "RB_Scorer_" + FROM_QUERY_PARAM + "_To_" + TO_QUERY_PARAM + ".csv";

  public static final ReportInstanceReferenceDto REPORT_INSTANCE_REFERENCE_DTO =
      new ReportInstanceReferenceDto(REPORT_ID);
}
