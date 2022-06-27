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
import static java.lang.String.join;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;

@Slf4j
@RequiredArgsConstructor
class UserAuthActivityReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String FILE_NAME_PREFIX = "SURVEILLANCE_OPTIMIZATION_Login_Logout_";
  private static final String LIST_ELEMENTS_JOINER = ",";
  private static final String ACCESS_COUNTRY_DEFAULT_VALUE = "Global";
  private static final String NO_TIMESTAMP_DEFAULT_VALUE = "";

  @NonNull
  private final DateRangeProvider dateRangeProvider;

  @NonNull
  private final UserAuthActivityEventProvider dataProvider;

  @NonNull
  private final String reportsDir;

  @NonNull
  private final FileLineWriter fileLineWriter;

  @NonNull
  private final TimeSource timeProvider;

  @Scheduled(cron = "${report-scb.user-auth-activity.cron}")
  void generate() throws IOException {
    DateRange dateRange = dateRangeProvider.latestDateRange();
    log.info("Generating user login/logout report for {}", dateRange);

    List<UserAuthActivityEventDto> userAuthActivityEvents =
        dataProvider.provide(dateRange.getFrom(), dateRange.getTo());

    fileLineWriter.write(reportsDir, fileName(), reportLines(userAuthActivityEvents));
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }

  private Stream<String> reportLines(List<UserAuthActivityEventDto> userAuthActivityEvents) {
    return new CsvBuilder<>(userAuthActivityEvents.stream())
        .cell("Access_ID", UserAuthActivityEventDto::getUsername)
        .cell("Access_Profile", event -> formatList(event.getRoles()))
        .cell("Access_Country", event -> ACCESS_COUNTRY_DEFAULT_VALUE)
        .cell("Access_SourceIP", UserAuthActivityEventDto::getIpAddress)
        .cell("Access_LoginTimeStamp", event -> formatDate(event.getLoginTimestamp()))
        .cell("Access_LogoutTimeStamp", event -> formatDate(event.getLogoutTimestamp()))
        .build()
        .lines();
  }

  private static String formatList(List<String> list) {
    return join(LIST_ELEMENTS_JOINER, list);
  }

  private String formatDate(long timestamp) {
    return timestamp > 0 ?
           TIMESTAMP_FORMATTER.format(
               ofInstant(ofEpochMilli(timestamp), timeProvider.timeZone().toZoneId()))
           : NO_TIMESTAMP_DEFAULT_VALUE;
  }
}
