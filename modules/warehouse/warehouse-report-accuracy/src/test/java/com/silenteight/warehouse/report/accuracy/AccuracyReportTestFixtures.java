package com.silenteight.warehouse.report.accuracy;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.parse;
import static java.util.List.of;

@NoArgsConstructor
public final class AccuracyReportTestFixtures {

  public static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  public static final String PRODUCTION_ANALYSIS_NAME = "production";
  public static final String REPORT_CONTENT = "test row";
  public static final String DATE_FIELD_NAME = "index_timestamp";
  public static final String HIT_TYPE_FIELD_NAME = "alert_categories/hitType:value";
  public static final String HIT_TYPE_FIELD_LABEL = "Hit Type";
  public static final String COUNTRY_FIELD_NAME = "s8_country";
  public static final String COUNTRY_FIELD_LABEL = "Country";
  public static final String ANALYST_FIELD_NAME = "alert_analyst_decision";
  public static final String ANALYST_FIELD_POSITIVE_VALUE = "analyst_decision_true_positive";
  public static final String ANALYST_FIELD_NEGATIVE_VALUE = "analyst_decision_false_positive";
  public static final String FROM_QUERY_PARAM = "2019-04-11";
  public static final String TO_QUERY_PARAM = "2021-04-11";
  public static final LocalDate LOCAL_DATE_FROM = parse(FROM_QUERY_PARAM);
  public static final LocalDate LOCAL_DATE_TO = parse(TO_QUERY_PARAM);
  public static final ReportRange REPORT_RANGE = ReportRange.of(LOCAL_DATE_FROM, LOCAL_DATE_TO);
  public static final List<String> INDEXES = of("index213");
  public static final String FILE_STORAGE_NAME = "ed4e4857-14af-4ed6-bd53-a12e36f86856";
  public static final long REPORT_ID = 9;
  public static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(REPORT_ID);

  public static final String PRODUCTION_REPORT_FILENAME =
      "Accuracy_" + FROM_QUERY_PARAM + "_To_" + TO_QUERY_PARAM + ".csv";

  public static final String SIMULATION_REPORT_FILENAME =
      "simulation_" + ANALYSIS_ID + "_Accuracy.csv";

  public static final AccuracyReportDto ACCURACY_REPORT_DTO =
      AccuracyReportDto.builder()
          .fileStorageName(FILE_STORAGE_NAME)
          .range(REPORT_RANGE)
          .build();
}
