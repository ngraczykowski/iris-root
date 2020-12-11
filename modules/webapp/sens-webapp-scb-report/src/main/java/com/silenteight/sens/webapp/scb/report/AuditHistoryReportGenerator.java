package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.common.support.file.FileLineWriter;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;

@Slf4j
@RequiredArgsConstructor
class AuditHistoryReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String FILE_NAME_PREFIX = "SURVILLANCE_OPTIMIZATION_AuditHistory_";
  private static final String AUDIT_COUNTRY_DEFAULT_VALUE = "Global";

  @NonNull
  private final DateRangeProvider dateRangeProvider;

  @NonNull
  private final AuditHistoryEventProvider auditHistoryEventProvider;

  @NonNull
  private final String reportsDir;

  @NonNull
  private final FileLineWriter fileLineWriter;

  @NonNull
  private final TimeSource timeProvider;

  @Scheduled(cron = "${report-scb.audit-history.cron}")
  void generate() throws IOException {
    DateRange dateRange = dateRangeProvider.latestDateRange();
    log.info("Generating audit history report for {}", dateRange);

    List<AuditHistoryEventDto> auditHistoryEvents =
        auditHistoryEventProvider.provide(dateRange.getFrom(), dateRange.getTo());

    fileLineWriter.write(reportsDir, fileName(), reportLinesFrom(auditHistoryEvents));
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }

  private Stream<String> reportLinesFrom(List<AuditHistoryEventDto> auditHistoryEvents) {
    return new CsvBuilder<>(auditHistoryEvents.stream())
        .cell("Audit_ID", AuditHistoryEventDto::getUsername)
        .cell("Audit_Status", AuditHistoryEventDto::getStatus)
        .cell("Access_SourceIP", AuditHistoryEventDto::getIpAddress)
        .cell("Audit_Country", event -> AUDIT_COUNTRY_DEFAULT_VALUE)
        .cell("Audit_LoginTimeStamp", event -> formatDate(event.getTimestamp()))
        .build()
        .lines();
  }

  private String formatDate(long timestamp) {
    return TIMESTAMP_FORMATTER.format(
        ofInstant(ofEpochMilli(timestamp), timeProvider.timeZone().toZoneId()));
  }
}
