package com.silenteight.sens.webapp.scb.report;


import com.silenteight.sep.usermanagement.api.event.EventQuery;
import com.silenteight.sep.usermanagement.api.event.EventType;
import com.silenteight.sep.usermanagement.api.event.dto.EventDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN_ERROR;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditHistoryEventProviderTest {

  private static final String IP_ADDRESS = "191.15.0.7";
  private static final String SUCCESS_STATUS = "SUCCESS";
  private static final String FAILURE_STATUS = "FAILED";

  @Mock
  private EventQuery eventQuery;

  @InjectMocks
  private AuditHistoryEventProvider underTest;

  @Test
  void returnsAuditHistoryEventsFromAuditData() {
    OffsetDateTime from = now().minusHours(6);
    OffsetDateTime to = now();
    String userName = "jdoe";
    long loginErrorTimestamp = createPastTimestamp(to, 40);
    long loginTimestamp = createPastTimestamp(to, 38);
    long extendSessionTimestamp = createPastTimestamp(to, 30);

    when(eventQuery.list(from, List.of(LOGIN, LOGIN_ERROR)))
        .thenReturn(
            List.of(
                makeLoginErrorEventDto(userName, loginErrorTimestamp),
                makeLoginEventDto(userName, loginTimestamp),
                makeExtendSessionEventDto(extendSessionTimestamp)));

    List<AuditHistoryEventDto> events = underTest.provide(from, to);

    assertThat(events).hasSize(2);
    assertThat(events).isEqualTo(
        List.of(
            AuditHistoryEventDto.builder()
                .username(userName)
                .status(FAILURE_STATUS)
                .ipAddress(IP_ADDRESS)
                .timestamp(loginErrorTimestamp)
                .build(),
            AuditHistoryEventDto.builder()
                .username(userName)
                .status(SUCCESS_STATUS)
                .ipAddress(IP_ADDRESS)
                .timestamp(loginTimestamp)
                .build()));
  }

  private static long createPastTimestamp(OffsetDateTime dateTime, int minutes) {
    return dateTime.minusMinutes(minutes).toInstant().toEpochMilli();
  }

  private static EventDto makeLoginErrorEventDto(String userName, long timestamp) {
    return makeEventDto(LOGIN_ERROR, userName, timestamp);
  }

  private static EventDto makeLoginEventDto(String userName, long timestamp) {
    return makeEventDto(LOGIN, userName, timestamp);
  }

  private static EventDto makeExtendSessionEventDto(long timestamp) {
    return makeEventDto(LOGIN, null, timestamp);
  }

  private static EventDto makeEventDto(EventType eventType, String userName, long timestamp) {
    return EventDto.builder()
        .type(eventType)
        .userName(userName)
        .ipAddress(IP_ADDRESS)
        .timestamp(timestamp)
        .build();
  }
}
