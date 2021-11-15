package com.silenteight.warehouse.report.reasoning;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reasoning.domain.dto.AiReasoningReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.parse;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AiReasoningReportTestFixtures {

  public static final String QUERY_PARAM_FROM = "2020-08-15";
  public static final String QUERY_PARAM_TO = "2021-10-15";
  public static final OffsetDateTime OFFSET_DATE_TIME_TO = of(parse(QUERY_PARAM_TO), MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM =
      of(parse(QUERY_PARAM_FROM), MIDNIGHT, UTC);

  public static final ReportRange REPORT_RANGE = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);
  public static final String ANALYSIS_ID = "8ce609a3-660a-4808-9c39-fdf425793070";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_CONTENT = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String ALERT_STATUS_FIELD_NAME = "alert_status";
  public static final String ALERT_STATUS_FIELD_LABEL = "Alert Status";
  public static final String ALERT_COMMENT_FIELD_NAME = "alert_comment";
  public static final String ALERT_COMMENT_FIELD_LABEL = "Alert Comment";
  public static final String FILE_STORAGE_NAME = "7758194f-6d25-4ae0-9080-68b123ba0637";
  public static final List<String> INDEXES = of("index444");
  public static final long REPORT_ID = 1;
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final String PRODUCTION_REPORT_FILENAME =
      "AI_Reasoning_" + QUERY_PARAM_FROM + "_To_" + QUERY_PARAM_TO + ".csv";

  public static final String SIMULATION_REPORT_FILENAME =
      "simulation_" + ANALYSIS_ID + "_AI_Reasoning.csv";

  public static final AiReasoningReportDto AI_REASONING_REPORT_DTO =
      AiReasoningReportDto.builder()
          .fileStorageName(FILE_STORAGE_NAME)
          .range(REPORT_RANGE)
          .build();
}
