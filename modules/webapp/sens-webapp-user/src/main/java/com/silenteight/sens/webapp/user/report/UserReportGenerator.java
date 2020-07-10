package com.silenteight.sens.webapp.user.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.ReportGenerator;
import com.silenteight.sens.webapp.report.SimpleReport;
import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import io.vavr.control.Option;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class UserReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "user-report";
  private static final String MUTLIVALUE_FILED_SEPARATOR = ",";

  static final String USERNAME_COLUMN_HEADER = "user_name";
  static final String DISPLAY_NAME_COLUMN_HEADER = "display_name";
  static final String TYPE_COLUMN_HEADER = "type";
  static final String ROLES_COLUMN_HEADER = "roles";
  static final String LAST_LOGIN_AT_COLUMN_HEADER = "last_login_at";
  static final String CREATED_AT_COLUMN_HEADER = "created_at";

  @NonNull
  private final TimeSource timeProvider;
  @NonNull
  private final DateFormatter fileNameDateFormatter;
  @NonNull
  private final DateFormatter rowDateFormatter;
  @NonNull
  private final UserListQuery userListQuery;

  @Override
  public String getName() {
    return REPORT_NAME;
  }

  @Override
  public Report generateReport(Map<String, String> parameters) {
    return new SimpleReport(getReportFileName(), getReportData());
  }

  private static String rolesFormatter(UserDto user) {
    return String.join(MUTLIVALUE_FILED_SEPARATOR, user.getRoles());
  }

  private String getReportFileName() {
    return format("%s-%s.csv", REPORT_NAME, getFormattedCurrentDate());
  }

  private String getFormattedCurrentDate() {
    return fileNameDateFormatter.format(timeProvider.now());
  }

  private List<String> getReportData() {
    Collection<UserDto> users = userListQuery.listEnabled();
    return buildReportData(users).collect(toList());
  }

  private Stream<String> buildReportData(Collection<UserDto> users) {
    return new CsvBuilder<>(users.stream())
        .cell(USERNAME_COLUMN_HEADER, UserDto::getUserName)
        .cell(DISPLAY_NAME_COLUMN_HEADER, UserDto::getDisplayName)
        .cell(TYPE_COLUMN_HEADER, UserDto::getOrigin)
        .cell(ROLES_COLUMN_HEADER, UserReportGenerator::rolesFormatter)
        .cell(LAST_LOGIN_AT_COLUMN_HEADER, dateFormatter(UserDto::getLastLoginAt))
        .cell(CREATED_AT_COLUMN_HEADER, dateFormatter(UserDto::getCreatedAt))
        .build()
        .lines();
  }

  private Function<UserDto, String> dateFormatter(Function<UserDto, OffsetDateTime> dateSupplier) {
    return u -> Option.of(dateSupplier.apply(u))
        .map(rowDateFormatter::format)
        .getOrNull();
  }
}
