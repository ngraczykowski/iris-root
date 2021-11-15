package com.silenteight.warehouse.report.reasoning.match;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.domain.dto.AiReasoningMatchLevelReportDto;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AiReasoningMatchLevelReportTestFixtures {

  public static final String FROM_QUERY_PARAM = "2015-08-25";
  public static final String TO_QUERY_PARAM = "2017-12-24";
  public static final LocalDate LOCAL_DATE_FROM = parse(FROM_QUERY_PARAM);
  public static final LocalDate LOCAL_DATE_TO = parse(TO_QUERY_PARAM);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM = of(LOCAL_DATE_FROM, MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_TO = of(LOCAL_DATE_TO, MIDNIGHT, UTC);
  public static final ReportRange REPORT_RANGE = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);
  public static final String ANALYSIS_ID = "070be771-2107-4ac2-b6f2-81899364aeea";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_CONTENT = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String ALERT_STATUS_FIELD_NAME = "alert_status";
  public static final String ALERT_STATUS_FIELD_LABEL = "Alert Status";
  public static final String ALERT_COMMENT_FIELD_NAME = "alert_comment";
  public static final String ALERT_COMMENT_FIELD_LABEL = "Alert Comment";
  public static final String FILE_STORAGE_NAME = "0931655c-6b3e-4e50-a27e-85226ab16ca2";
  public static final List<String> INDEXES = of("index999");
  public static final long REPORT_ID = 109;
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final String PRODUCTION_REPORT_FILENAME =
      "AI_Reasoning_Match_Level_" + FROM_QUERY_PARAM + "_To_" + TO_QUERY_PARAM + ".csv";

  public static final String SIMULATION_REPORT_FILENAME =
      "simulation_" + ANALYSIS_ID + "_AI_Reasoning_Match_Level.csv";

  public static final AiReasoningMatchLevelReportDto AI_REASONING_MATCH_LEVEL_REPORT_DTO =
      AiReasoningMatchLevelReportDto.builder()
          .fileStorageName(FILE_STORAGE_NAME)
          .range(REPORT_RANGE)
          .build();
}
