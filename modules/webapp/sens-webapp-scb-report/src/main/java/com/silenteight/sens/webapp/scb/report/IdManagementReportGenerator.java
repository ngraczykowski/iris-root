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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.sep.base.common.time.ApplicationTimeZone.TIME_ZONE;

@RequiredArgsConstructor
@Slf4j
class IdManagementReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String FILE_NAME_PREFIX = "SURVILLANCE_OPTIMIZATION_ID_Management_";
  private static final String APP_ID_COUNTRY_DEFAULT_VALUE = "Global";

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

  private static Stream<String> reportLinesFrom(List<IdManagementEventDto> idManagementEvents) {
    return new CsvBuilder<>(idManagementEvents.stream())
        .cell("AppID_ID", IdManagementEventDto::getUsername)
        .cell("AppID_Name", IdManagementEventDto::getRole)
        .cell("AppID_Implemented_By", IdManagementEventDto::getPrincipal)
        .cell("AppID_Implemented_TimeStamp", event -> formatDate(event.getTimestamp()))
        .cell("AppID_Status", IdManagementEventDto::getAction)
        .cell("AppID_Country", event -> APP_ID_COUNTRY_DEFAULT_VALUE)
        .build()
        .lines();
  }

  private static String formatDate(Instant timestamp) {
    return TIMESTAMP_FORMATTER.format(timestamp);
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }
}
