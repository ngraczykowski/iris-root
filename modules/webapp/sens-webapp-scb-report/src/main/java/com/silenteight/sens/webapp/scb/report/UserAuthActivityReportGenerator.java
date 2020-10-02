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
import static java.lang.String.format;
import static java.lang.String.join;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;

@Slf4j
@RequiredArgsConstructor
class UserAuthActivityReportGenerator {

  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss").withZone(TIME_ZONE.toZoneId());

  private static final String FILE_NAME_PREFIX =
      "SURVILLANCE_OPTIMIZATION_Login_Logout_";
  private static final String LIST_ELEMENTS_JOINER = ",";
  static final String ACCESS_COUNTRY_DEFAULT_VALUE = "Global";

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

    fileLineWriter.write(reportsDir, fileName(), reportLines(dateRange));
  }

  private String fileName() {
    DateFormatter fileNameDateFormatter = new DigitsOnlyDateFormatter();
    return FILE_NAME_PREFIX + fileNameDateFormatter.format(timeProvider.now()) + ".csv";
  }

  private Stream<String> reportLines(DateRange dateRange) {
    List<UserAuthActivityEventDto> data =
        dataProvider.provide(dateRange.getFrom(), dateRange.getTo());

    return new CsvBuilder<>(data.stream())
        .delimiter("\t")
        .cell("Access_ID,", event -> commaSuffixed(event.getUserName()))
        .cell("Access_Profile,", event -> commaSuffixed(formatList(event.getRoles())))
        .cell("Access_Country,", event -> commaSuffixed(ACCESS_COUNTRY_DEFAULT_VALUE))
        .cell("Access_SourceIP,", event -> commaSuffixed(wrapInQuotes(event.getIpAddress())))
        .cell("Access_LoginTimeStamp,", event -> formatLogin(event.getLoginTimestamp()))
        .cell("Access_LogoutTimeStamp", event -> formatLogout(event.getLogoutTimestamp()))
        .build()
        .lines();
  }

  //SCB specific requirement
  private static String commaSuffixed(String value) {
    return format("%s,", value);
  }

  private static String wrapInQuotes(String value) {
    return "\"" + value + "\"";
  }

  private static String formatList(List<String> list) {
    return join(LIST_ELEMENTS_JOINER, list);
  }

  private String formatLogin(long timestamp) {
    return timestamp > 0 ? commaSuffixed(wrapInQuotes(formatDate(timestamp))) : "";
  }

  private String formatLogout(long timestamp) {
    return timestamp > 0 ? wrapInQuotes(formatDate(timestamp)) : "";
  }

  private String formatDate(long timestamp) {
    return TIMESTAMP_FORMATTER.format(
        ofInstant(ofEpochMilli(timestamp), timeProvider.timeZone().toZoneId()));
  }
}
