package com.silenteight.sens.webapp.scb.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.common.support.file.FileLineWriter;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;
import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
class IdManagementReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy  HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String FILE_NAME_PREFIX = "SURVILLANCE_OPTIMIZATION_ID_Management_";

  @NonNull
  private final DateRangeProvider dateRangeProvider;

  @NonNull
  private final IdManagementEventProvider idManagementEventProvider;

  @NonNull
  private final String reportsDir;

  @NonNull
  private final FileLineWriter lineWriter;

  @NonNull
  private final TimeSource timeProvider;

  @Scheduled(cron = "${report-scb.id-management.cron}")
  void generate() throws IOException {
    DateRange dateRange = dateRangeProvider.latestDateRange();
    log.info("Generating user management report for {}", dateRange);

    List<IdManagementEventDto> idManagementEvents =
        idManagementEventProvider.idManagementEvents(dateRange.getFrom(), dateRange.getTo());

    lineWriter.write(reportsDir, fileName(), reportLinesFrom(idManagementEvents));
  }

  private Stream<String> reportLinesFrom(List<IdManagementEventDto> idManagementEvents) {
    return new CsvBuilder<>(idManagementEvents.stream())
        .delimiter("\t")
        .cell("AppID_ID,", event -> commaSuffixed(event.getUsername()))
        .cell("AppID_Name,", event -> commaSuffixed(event.getRole()))
        .cell(
            "AppID_Implemented_By,", event -> commaSuffixed(event.getPrincipal()))
        .cell(
            "AppID_Implemented_TimeStamp,",
            event -> "\"" + commaSuffixed(TIMESTAMP_FORMATTER.format(event.getTimestamp())) + "\"")
        .cell("AppID_Status,", event -> commaSuffixed(event.getAction()))
        .cell("AppID_Country", a -> "Global")
        .build()
        .lines();
  }

  //SCB specific requirement
  private static String commaSuffixed(String value) {
    return format("%s,", value);
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }
}
