package com.silenteight.warehouse.report.rbs;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;
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
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RbsReportTestFixtures {

  public static final long REPORT_ID = 7;
  public static final String ANALYSIS_ID = "123";
  public static final String QUERY_PARAM_TO = "2021-10-15";
  public static final String QUERY_PARAM_FROM = "2021-08-15";
  public static final LocalDate LOCAL_DATE_TO = parse(QUERY_PARAM_TO);
  public static final LocalDate LOCAL_DATE_FROM = parse(QUERY_PARAM_FROM);
  public static final OffsetDateTime OFFSET_DATE_TIME_TO = of(LOCAL_DATE_TO, MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM = of(LOCAL_DATE_FROM, MIDNIGHT, UTC);
  public static final ReportRange REPORT_RANGE = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String POLICY_ID_FIELD_NAME = "alert_policyId";
  public static final String POLICY_ID_FIELD_LABEL = "policy_id";
  public static final String RECOMMENDATION_FIELD_NAME = "alert_s8_recommendation";
  public static final String RECOMMENDATION_FIELD_LABEL = "alert_recommendation";
  public static final String TEST_INDEX = "test_index";
  public static final List<String> INDEXES = List.of(TEST_INDEX);
  public static final String CREATION_TIMESTAMP = "1616574888888";
  public static final String CONTENT = "report content";
  public static final String REPORT_FILENAME =
      "RB_Scorer_Prod_" + QUERY_PARAM_FROM + "_To_" + QUERY_PARAM_TO + ".csv";

  public static final ReportInstanceReferenceDto REPORT_INSTANCE_REFERENCE_DTO =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final RbsReportDto RB_SCORER_REPORT_DTO =
      RbsReportDto.builder()
          .range(REPORT_RANGE)
          .content(CONTENT)
          .timestamp(CREATION_TIMESTAMP)
          .fileName(REPORT_FILENAME)
          .build();
}
