package com.silenteight.sens.webapp.user.report;

import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.silenteight.sens.webapp.user.report.ReportAssert.assertThat;
import static com.silenteight.sens.webapp.user.report.UserDtoFixtures.*;
import static com.silenteight.sens.webapp.user.report.UsersReportGenerator.*;
import static com.silenteight.sens.webapp.user.report.UsersReportGeneratorTest.UsersReportGeneratorFixtures.CURRENT_TIME;
import static com.silenteight.sens.webapp.user.report.UsersReportGeneratorTest.UsersReportGeneratorFixtures.EMPTY_VALUE;
import static com.silenteight.sens.webapp.user.report.UsersReportGeneratorTest.UsersReportGeneratorFixtures.FILENAME_DATE_PREFIX;
import static com.silenteight.sens.webapp.user.report.UsersReportGeneratorTest.UsersReportGeneratorFixtures.ROW_DATA_DATE_PREFIX;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.List.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersReportGeneratorTest {

  @Mock
  private UserListQuery userListQuery;

  private TestDateFormatter filenameDateFormatter;
  private TestDateFormatter rowDateFormatter;
  private UsersReportGenerator underTest;

  @BeforeEach
  void setUp() {
    filenameDateFormatter = new TestDateFormatter(FILENAME_DATE_PREFIX);
    rowDateFormatter = new TestDateFormatter(ROW_DATA_DATE_PREFIX);

    underTest = new UsersReportGenerator(
        new MockTimeSource(Instant.parse(CURRENT_TIME)),
        filenameDateFormatter,
        rowDateFormatter,
        userListQuery);
  }

  @Test
  void reportHasName() {
    String name = underTest.getName();

    Assertions.assertThat(name).isEqualTo("users-report");
  }

  @Test
  void reportFilenameContainsNameAndTimestamp() {
    when(userListQuery.listEnabled()).thenReturn(emptyList());

    Report report = underTest.generateReport(emptyMap());

    String expectedName = "users-report-" + filenameDateFormatter.format(CURRENT_TIME) + ".csv";
    assertThat(report).hasName(expectedName);
  }

  @Test
  void reportContainsProperHeaders() {
    when(userListQuery.listEnabled()).thenReturn(of(ACTIVE_USER));

    Report report = underTest.generateReport(emptyMap());

    assertThat(report).hasFields(USERNAME_COLUMN_HEADER, DISPLAY_NAME_COLUMN_HEADER,
        ORIGIN_COLUMN_HEADER, ROLES_COLUMN_HEADER, LAST_LOGIN_AT_COLUMN_HEADER,
        CREATED_AT_COLUMN_HEADER);
  }

  @Test
  void reportContainsData() {
    when(userListQuery.listEnabled()).thenReturn(of(ACTIVE_USER));

    Report report = underTest.generateReport(emptyMap());

    String expectedLastLoginAt = rowDateFormatter.format(LAST_LOGIN_AT);
    String expectedCreatedAt = rowDateFormatter.format(CREATED_AT);
    assertThat(report).hasValues(0, USERNAME, DISPLAY_NAME, ORIGIN, ROLE_1,
        expectedLastLoginAt, expectedCreatedAt);
  }

  @Test
  void reportContainsUserWithMultipleRoles() {
    when(userListQuery.listEnabled()).thenReturn(of(MULTIPLE_ROLES_USER));

    Report report = underTest.generateReport(emptyMap());

    String expectedRole = format("\"%s,%s\"", ROLE_1, ROLE_2);
    assertThat(report).hasValues(0, EMPTY_VALUE, EMPTY_VALUE, ORIGIN, expectedRole,
        EMPTY_VALUE, EMPTY_VALUE);
  }

  static class UsersReportGeneratorFixtures {

    static final String EMPTY_VALUE = "";
    static final String FILENAME_DATE_PREFIX = "FILENAME";
    static final String ROW_DATA_DATE_PREFIX = "ROW";
    static final String CURRENT_TIME = "2020-07-06T08:10:30Z";
  }
}