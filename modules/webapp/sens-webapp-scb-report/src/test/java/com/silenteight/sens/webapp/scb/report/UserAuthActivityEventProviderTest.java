package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.backend.report.domain.ReportMetadataService;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.EventQuery;
import com.silenteight.sep.usermanagement.api.dto.EventDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;
import static com.silenteight.sep.usermanagement.api.event.EventType.EXTEND_SESSION;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGOUT;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthActivityEventProviderTest {

  private static final String REPORT_NAME = "user-auth-activity";

  private static final String IP_ADDRESS = "191.15.0.7";

  private static final int REPORT_PERIOD_IN_MINUTES = 60;

  private static final List<String> USER_ROLES = List.of("ADMINISTRATOR", "AUDITOR");

  private static final int SESSION_TIMEOUT = 1800;

  private static final TimeSource TIME_SOURCE = ARBITRARY_INSTANCE;

  @Mock
  private ConfigurationQuery configurationQuery;

  @Mock
  private EventQuery eventQuery;

  @Mock
  private ReportMetadataService reportMetadataService;

  @Mock
  private UserRolesRetriever userRolesRetriever;

  private UserAuthActivityEventProvider underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserAuthActivityEventProvider(
        configurationQuery,
        eventQuery,
        reportMetadataService,
        userRolesRetriever,
        TIME_SOURCE.timeZone().toZoneId());

    when(configurationQuery.getSessionIdleTimeoutSeconds()).thenReturn(SESSION_TIMEOUT);
  }

  @Test
  void givenNoEvents_returnEmptyReportData() {
    // given
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(emptyList());

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).isEmpty();
  }

  @Test
  void givenTheSameUserEventsWithinOneReportRun_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 40);
    long logoutTimestamp = createPastTimestamp(to, 20);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp),
            makeLogoutEventDto(userId, logoutTimestamp)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(
            makeUserLoginLogoutActivityEventDto(userName, loginTimestamp, logoutTimestamp)));
  }

  @Test
  void givenTheSameUserEventsWithinTwoReportRuns_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 80);
    long loginTimestamp2 = createPastTimestamp(to, 40);
    long logoutTimestamp1 = createPastTimestamp(to, 70);
    long logoutTimestamp2 = createPastTimestamp(to, 20);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp1),
            makeLoginEventDto(codeId, userId, userName, loginTimestamp2),
            makeLogoutEventDto(userId, logoutTimestamp1),
            makeLogoutEventDto(userId, logoutTimestamp2)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(
            makeUserLoginLogoutActivityEventDto(userName, loginTimestamp2, logoutTimestamp2)));
  }

  @Test
  void givenOnlyLoginEventWithExpectedLogoutOlderThanSessionTimeout_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 40);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(makeLoginEventDto(codeId, userId, userName, loginTimestamp)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(
            makeUserLoginLogoutActivityEventDto(
                userName, loginTimestamp, createPastTimestamp(to, 10))));
  }

  @Test
  void givenOnlyLoginEventWithExpectedLogoutNewerThanSessionTimeout_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 20);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(makeLoginEventDto(codeId, userId, userName, loginTimestamp)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(makeUserLoginActivityEventDto(userName, loginTimestamp)));
    verify(reportMetadataService).saveStartTime(REPORT_NAME, to.minusMinutes(20));
  }

  @Test
  void givenUserLoginBeforeFromDateAndExtendSessionEventsAndNoLogoutEvent_returnEmptyReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 70);
    long extendSessionTimestamp1 = createPastTimestamp(to, 50);
    long extendSessionTimestamp2 = createPastTimestamp(to, 30);
    long extendSessionTimestamp3 = createPastTimestamp(to, 10);
    OffsetDateTime startTime = to.minusMinutes(70);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(startTime);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp),
            makeExtendSessionEventDto(codeId, userId, extendSessionTimestamp1),
            makeExtendSessionEventDto(codeId, userId, extendSessionTimestamp2),
            makeExtendSessionEventDto(codeId, userId, extendSessionTimestamp3)));

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).isEmpty();
  }

  @Test
  void givenTwoUsersEventsWithinOneReportRun_returnReportData() {
    // given
    String codeId1 = "ab12";
    String userId1 = "12345";
    String userName1 = "jdoe";
    String codeId2 = "xy89";
    String userId2 = "67890";
    String userName2 = "asmith";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 50);
    long loginTimestamp2 = createPastTimestamp(to, 40);
    long logoutTimestamp1 = createPastTimestamp(to, 35);
    long logoutTimestamp2 = createPastTimestamp(to, 38);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId1, userId1, userName1, loginTimestamp1),
            makeLoginEventDto(codeId2, userId2, userName2, loginTimestamp2),
            makeLogoutEventDto(userId1, logoutTimestamp1),
            makeLogoutEventDto(userId2, logoutTimestamp2)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(2);
    assertThat(data).isEqualTo(
        List.of(
            makeUserLoginLogoutActivityEventDto(userName1, loginTimestamp1, logoutTimestamp1),
            makeUserLoginLogoutActivityEventDto(userName2, loginTimestamp2, logoutTimestamp2)));
  }

  @Test
  void givenUserTwoLoginsEventsWithExpectedLogoutOlderThanSessionTimeout_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 50);
    long loginTimestamp2 = createPastTimestamp(to, 40);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp1),
            makeLoginEventDto(codeId, loginTimestamp2)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(
            makeUserLoginLogoutActivityEventDto(
                userName, loginTimestamp1, createPastTimestamp(to, 10))));
  }

  @Test
  void givenUserTwoLoginsEventsWithExpectedLogoutNewerThanSessionTimeout_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 50);
    long loginTimestamp2 = createPastTimestamp(to, 25);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp1),
            makeLoginEventDto(codeId, loginTimestamp2)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(makeUserLoginActivityEventDto(userName, loginTimestamp1)));
    verify(reportMetadataService).saveStartTime(REPORT_NAME, to.minusMinutes(50));
  }

  @Test
  void givenUserLoginAndExtendSessionEventsWithLogoutOlderThanSessionTimeout_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 50);
    long extendSessionTimestamp = createPastTimestamp(to, 40);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp),
            makeExtendSessionEventDto(codeId, userId, extendSessionTimestamp)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(
            makeUserLoginLogoutActivityEventDto(
                userName, loginTimestamp, createPastTimestamp(to, 10))));
  }

  @Test
  void givenUserLoginAndExtendSessionEventsWithLogoutNewerThanSessionTmt_returnReportData() {
    // given
    String codeId = "ab12";
    String userId = "12345";
    String userName = "jdoe";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp = createPastTimestamp(to, 50);
    long extendSessionTimestamp = createPastTimestamp(to, 25);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId, userId, userName, loginTimestamp),
            makeExtendSessionEventDto(codeId, userId, extendSessionTimestamp)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(1);
    assertThat(data).isEqualTo(
        singletonList(makeUserLoginActivityEventDto(userName, loginTimestamp)));
    verify(reportMetadataService).saveStartTime(REPORT_NAME, to.minusMinutes(50));
  }

  @Test
  void givenMultipleEventsScenario_returnReportData() {
    // given
    String codeId1 = "ab12";
    String userId1 = "12345";
    String userName1 = "jdoe";
    String codeId2 = "xy89";
    String userId2 = "67890";
    String userName2 = "asmith";
    String codeId3 = "ef56";
    String userId3 = "24680";
    String userName3 = "jkowalsky";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 56);
    long loginTimestamp2 = createPastTimestamp(to, 54);
    long loginTimestamp3 = createPastTimestamp(to, 52);
    long loginTimestamp4 = createPastTimestamp(to, 45);
    long logoutTimestamp1 = createPastTimestamp(to, 30);
    long extendSessionTimestamp2 = createPastTimestamp(to, 38);
    long extendSessionTimestamp3 = createPastTimestamp(to, 35);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(null);
    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId1, userId1, userName1, loginTimestamp1),
            makeLoginEventDto(codeId2, userId2, userName2, loginTimestamp2),
            makeLoginEventDto(codeId3, userId3, userName3, loginTimestamp3),
            makeLoginEventDto(codeId2, loginTimestamp4),
            makeLogoutEventDto(userId1, logoutTimestamp1),
            makeExtendSessionEventDto(codeId2, userId2, extendSessionTimestamp2),
            makeExtendSessionEventDto(codeId3, userId3, extendSessionTimestamp3)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(3);
    assertThat(data).isEqualTo(
        List.of(
            makeUserLoginLogoutActivityEventDto(
                userName1, loginTimestamp1, logoutTimestamp1),
            makeUserLoginLogoutActivityEventDto(
                userName2, loginTimestamp2, createPastTimestamp(to, 8)),
            makeUserLoginLogoutActivityEventDto(
                userName3, loginTimestamp3, createPastTimestamp(to, 5))));
  }

  @Test
  void givenMultipleEventsScenarioWithCustomReportStartTime_returnReportData() {
    // given
    String codeId1 = "ab12";
    String userId1 = "12345";
    String userName1 = "jdoe";
    String codeId2 = "xy89";
    String userId2 = "67890";
    String userName2 = "asmith";
    String codeId3 = "ef56";
    String userId3 = "24680";
    String userName3 = "jkowalsky";
    String codeId4 = "mn51";
    String userId4 = "13579";
    String userName4 = "pbest";
    String codeId5 = "uw93";
    String userId5 = "47924";
    String userName5 = "oyew";
    OffsetDateTime to = TIME_SOURCE.offsetDateTime();
    OffsetDateTime from = to.minusMinutes(REPORT_PERIOD_IN_MINUTES);
    long loginTimestamp1 = createPastTimestamp(to, 100);
    long loginTimestamp2 = createPastTimestamp(to, 95);
    long loginTimestamp3 = createPastTimestamp(to, 80);
    long loginTimestamp4 = createPastTimestamp(to, 75);
    long loginTimestamp5 = createPastTimestamp(to, 40);
    long logoutTimestamp1 = createPastTimestamp(to, 55);
    long logoutTimestamp4 = createPastTimestamp(to, 65);
    long extendSessionTimestamp1 = createPastTimestamp(to, 75);
    OffsetDateTime startTime = to.minusMinutes(100);
    when(reportMetadataService.getStartTime(REPORT_NAME)).thenReturn(startTime);

    when(eventQuery.getEvents(any(), any())).thenReturn(
        List.of(
            makeLoginEventDto(codeId1, userId1, userName1, loginTimestamp1),
            makeLoginEventDto(codeId2, userId2, userName2, loginTimestamp2),
            makeLoginEventDto(codeId3, userId3, userName3, loginTimestamp3),
            makeLoginEventDto(codeId4, userId4, userName4, loginTimestamp4),
            makeLoginEventDto(codeId5, userId5, userName5, loginTimestamp5),
            makeLogoutEventDto(userId1, logoutTimestamp1),
            makeLogoutEventDto(userId4, logoutTimestamp4),
            makeExtendSessionEventDto(codeId1, userId1, extendSessionTimestamp1)));
    when(userRolesRetriever.rolesOf(anyString())).thenReturn(USER_ROLES);

    // when
    List<UserAuthActivityEventDto> data = underTest.provide(from, to);

    // then
    assertThat(data).hasSize(3);
    assertThat(data).isEqualTo(
        List.of(
            makeUserLoginLogoutActivityEventDto(
                userName1, loginTimestamp1, logoutTimestamp1),
            makeUserLoginLogoutActivityEventDto(
                userName3, loginTimestamp3, createPastTimestamp(to, 50)),
            makeUserLoginLogoutActivityEventDto(
                userName5, loginTimestamp5, createPastTimestamp(to, 10))));
    verify(reportMetadataService).saveStartTime(REPORT_NAME, to.minusMinutes(95));
  }

  private static long createPastTimestamp(OffsetDateTime dateTime, int minutes) {
    return dateTime.minusMinutes(minutes).toInstant().toEpochMilli();
  }

  private static EventDto makeLoginEventDto(String codeId, long timestamp) {
    return makeLoginEventDto(codeId, null, null, timestamp);
  }

  private static EventDto makeLoginEventDto(
      String codeId, String userId, String userName, long timestamp) {

    return EventDto.builder()
        .type(LOGIN)
        .codeId(codeId)
        .ipAddress(IP_ADDRESS)
        .userId(userId)
        .userName(userName)
        .timestamp(timestamp)
        .build();
  }

  private static EventDto makeExtendSessionEventDto(String codeId, String userId, long timestamp) {
    return EventDto.builder()
        .type(EXTEND_SESSION)
        .codeId(codeId)
        .ipAddress(IP_ADDRESS)
        .userId(userId)
        .timestamp(timestamp)
        .build();
  }

  private static EventDto makeLogoutEventDto(String userId, long timestamp) {
    return EventDto.builder()
        .type(LOGOUT)
        .ipAddress(IP_ADDRESS)
        .userId(userId)
        .timestamp(timestamp)
        .build();
  }

  private static UserAuthActivityEventDto makeUserLoginActivityEventDto(
      String userName, long loginTimestamp) {

    return UserAuthActivityEventDto.builder()
        .username(userName)
        .roles(USER_ROLES)
        .ipAddress(IP_ADDRESS)
        .loginTimestamp(loginTimestamp)
        .build();
  }

  private static UserAuthActivityEventDto makeUserLoginLogoutActivityEventDto(
      String userName, long loginTimestamp, long logoutTimestamp) {

    return UserAuthActivityEventDto.builder()
        .username(userName)
        .roles(USER_ROLES)
        .ipAddress(IP_ADDRESS)
        .loginTimestamp(loginTimestamp)
        .logoutTimestamp(logoutTimestamp)
        .build();
  }
}
