package com.silenteight.warehouse.report.billing.v1.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.warehouse.report.billing.v1.domain.exception.ReportTypeNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;
import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static com.silenteight.sep.base.common.time.DefaultTimeSource.TIME_CONVERTER;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public enum DeprecatedReportDefinition {

  THIS_YEAR(
      "3aa046a1-6c0f-4ac2-bd79-635147db1e01",
      "Billing report - this year",
      getStartOfYear(),
      INSTANCE.now().atOffset(ZoneOffset.UTC)),
  PREVIOUS_YEAR(
      "3d71de48-76a0-4072-9e64-af00af626313",
      "Billing report - previous year",
      getStartOfPreviousYear(),
      getStartOfYear());

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(
          TIME_ZONE.toZoneId());

  @NonNull
  private static OffsetDateTime getStartOfYear() {
    return Year
        .from(TIME_CONVERTER.toOffset(INSTANCE.now()))
        .atMonth(1)
        .atDay(1)
        .atStartOfDay()
        .atOffset(ZoneOffset.UTC);
  }

  @NonNull
  private static OffsetDateTime getStartOfPreviousYear() {
    return Year
        .from(TIME_CONVERTER.toOffset(INSTANCE.now()))
        .minusYears(1)
        .atMonth(1)
        .atDay(1)
        .atStartOfDay()
        .atOffset(ZoneOffset.UTC);
  }

  private static final String REPORT_TYPE = "BILLING";
  private static final String FILENAME = "billing-report(%s-%s).csv";
  @NonNull
  private final String id;
  @NonNull
  private final String title;
  @NonNull
  private final OffsetDateTime from;
  @NonNull
  private final OffsetDateTime to;

  public static List<ReportDefinitionDto> toReportsDefinitionDto() {
    return stream(DeprecatedReportDefinition.values())
        .map(DeprecatedReportDefinition::toReportDefinitionDto)
        .collect(toList());
  }

  public static DeprecatedReportDefinition getReportType(String id) {
    return stream(values())
        .filter(reportDefinition -> reportDefinition.hasId(id))
        .findAny()
        .orElseThrow(() -> new ReportTypeNotFoundException(id));
  }

  private ReportDefinitionDto toReportDefinitionDto() {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .name(getReportName(id))
        .title(title)
        .description(generateDescription())
        .reportType(REPORT_TYPE)
        .build();
  }

  private String generateDescription() {
    return format("Billing report from %s to %s", FORMATTER.format(from), FORMATTER.format(to));
  }

  private String getReportName(String id) {
    return "analysis/production/definitions/" + REPORT_TYPE + "/" + id + "/reports";
  }

  private boolean hasId(String id) {
    return this.getId().equals(id);
  }

  public String getFilename() {
    return format(FILENAME, asFilenameString(from), asFilenameString(to));
  }

  private String asFilenameString(@NonNull OffsetDateTime value) {
    return DigitsOnlyDateFormatter.INSTANCE.format(value);
  }
}
