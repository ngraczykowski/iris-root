package com.silenteight.sens.webapp.backend.reportscb;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.user.dto.UserDto;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Stream;

import static com.silenteight.sens.webapp.backend.reportscb.UserFixtures.MULTIPLE_ROLES_USERS;
import static com.silenteight.sens.webapp.backend.reportscb.UserFixtures.SINGLE_ROLE_USERS;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsReportGeneratorTest {

  @Mock
  private UserListRepository repository;

  private AccountsReportGenerator underTest;

  @BeforeEach
  void setUp() {
    underTest = new AccountsReportGenerator(
        repository,
        new MockTimeSource(Instant.parse("2011-12-03T10:15:30Z")),
        new ScbReportDateFormatter());
  }

  @ParameterizedTest
  @MethodSource("reportTestCases")
  void reportTest(Collection<UserDto> users, String expectedReportFile) throws Exception {
    //given
    when(repository.list()).thenReturn(users);

    //when
    Report report = underTest.generateReport();

    //then
    assertThat(report.getReportFileName()).isEqualTo("accounts-report.csv");
    assertThat(getReportBody(report)).isEqualTo(getResourceAsString(expectedReportFile));
  }

  private static Stream<Arguments> reportTestCases() {
    return Stream.of(
        Arguments.of(emptyList(), "webapp/backend/report/husa/accounts/headerOnly.csv"),
        Arguments.of(SINGLE_ROLE_USERS,
            "webapp/backend/report/husa/accounts/singleRole.csv"),
        Arguments.of(MULTIPLE_ROLES_USERS,
            "webapp/backend/report/husa/accounts/multipleRoles.csv"));
  }

  @NotNull
  private String getReportBody(Report report) {
    return report.getReportContent().lines().collect(joining(""));
  }

  @NotNull
  private String getResourceAsString(String expectedReportFile) throws Exception {
    InputStream reportStream = getClass().getClassLoader().getResourceAsStream(expectedReportFile);
    return new String(reportStream.readAllBytes());
  }
}
