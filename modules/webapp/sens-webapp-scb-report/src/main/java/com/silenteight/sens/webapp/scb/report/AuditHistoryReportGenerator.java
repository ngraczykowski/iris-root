package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.support.csv.FileLineWriter;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;
import static java.io.File.separator;

@Slf4j
@RequiredArgsConstructor
class AuditHistoryReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String AUDIT_COUNTRY_DEFAULT_VALUE = "Global";
  private static final String FILE_NAME_PREFIX = "SURVILLANCE_OPTIMIZATION_AuditHistory_";
  private static final String REPORT_HEADER =
      "Audit_ID,Audit_Status,Access_SourceIP,Audit_Country,Audit_LoginTimeStamp";
  private static final String DELIMITER = ",";

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

    fileLineWriter.write(filePath(), reportLines(dateRange));
  }

  private String filePath() {
    return reportsDir + separator + fileName();
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }

  private Stream<String> reportLines(DateRange dateRange) {
    List<String> lines = new ArrayList<>();
    lines.add(wrapInQuotes(REPORT_HEADER));

    auditHistoryEventProvider
        .provide(dateRange.getFrom(), dateRange.getTo())
        .forEach(event -> lines.add(toReportLine(event)));

    return lines.stream();
  }

  private static final String toReportLine(AuditHistoryEventDto event) {
    List<String> line = new ArrayList<>();
    line.add(event.getUserName());
    line.add(event.getStatus());
    line.add(event.getIpAddress());
    line.add(AUDIT_COUNTRY_DEFAULT_VALUE);
    line.add(wrapInDoubleQuotes(TIMESTAMP_FORMATTER.format(event.getTimestamp())));

    return wrapInQuotes(String.join(DELIMITER, line));
  }

  private static String wrapInDoubleQuotes(String value) {
    return wrapInQuotes(wrapInQuotes(value));
  }

  private static String wrapInQuotes(String value) {
    return "\"" + value + "\"";
  }
}
