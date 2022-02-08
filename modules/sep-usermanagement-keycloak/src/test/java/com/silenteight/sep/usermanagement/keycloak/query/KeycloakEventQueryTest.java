package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.usermanagement.api.event.EventType;
import com.silenteight.sep.usermanagement.api.event.dto.EventDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.usermanagement.api.event.EventType.EXTEND_SESSION;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN_ERROR;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGOUT;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakEventQueryTest {

  private static final String LOGIN_EVENT_TYPE = "LOGIN";
  private static final String LOGIN_ERROR_EVENT_TYPE = "LOGIN_ERROR";
  private static final String LOGOUT_EVENT_TYPE = "LOGOUT";
  private static final String EXTEND_SESSION_EVENT_TYPE = "REFRESH_TOKEN";

  private static final String FRONTEND_CLIENT_ID = "frontend";
  private static final String BACKEND_CLIENT_ID = "backend";
  private static final String IP_ADDRESS = "192.138.0.12";

  @InjectMocks
  private KeycloakEventQuery underTest;

  @Mock
  private RealmResource realmResource;

  @Test
  void givenKeycloakEvents_returnEvents() {
    // given
    OffsetDateTime from = parse("2020-05-28T12:42:15+01:00");
    String userId = "abcd";
    String userName = "jdoe";
    String frontendEventCodeId = "123";
    String backendEventCodeId = "567";
    long loginTime = 156896544L;
    long loginErrorTime = 156921137L;
    long logoutTime = 156947890L;
    long extendSessionTime1 = 156900543L;
    long extendSessionTime2 = 156927211L;
    when(realmResource.getEvents(
        List.of(
            LOGIN_EVENT_TYPE,
            LOGIN_ERROR_EVENT_TYPE,
            LOGOUT_EVENT_TYPE,
            EXTEND_SESSION_EVENT_TYPE),
        null,
        null,
        "2020-05-28+01:00",
        null,
        null,
        null,
        null))
        .thenReturn(
            List.of(
                frontendEventRepresentation(
                    LOGIN_EVENT_TYPE,
                    userId,
                    loginTime,
                    Map.of("code_id", frontendEventCodeId, "username", userName)),
                frontendEventRepresentation(
                    LOGIN_ERROR_EVENT_TYPE,
                    userId,
                    loginErrorTime,
                    Map.of("code_id", frontendEventCodeId, "username", userName)),
                backendEventRepresentation(
                    EXTEND_SESSION_EVENT_TYPE,
                    userId,
                    extendSessionTime1,
                    Map.of("code_id", backendEventCodeId)),
                frontendEventRepresentation(
                    EXTEND_SESSION_EVENT_TYPE,
                    userId,
                    extendSessionTime2,
                    Map.of("code_id", frontendEventCodeId)),
                frontendEventRepresentation(
                    LOGOUT_EVENT_TYPE,
                    userId,
                    logoutTime,
                    Map.of("code_id", frontendEventCodeId))));

    // when
    List<EventDto> events = underTest.list(
        from, List.of(LOGIN, LOGIN_ERROR, LOGOUT, EXTEND_SESSION));

    // then
    assertThat(events).hasSize(4);
    assertThat(events).isEqualTo(
        List.of(
            loginEventDto(frontendEventCodeId, userId, userName, loginTime),
            loginErrorEventDto(frontendEventCodeId, userId, userName, loginErrorTime),
            logoutEventDto(frontendEventCodeId, userId, logoutTime),
            extendSessionEventDto(frontendEventCodeId, userId, extendSessionTime2)));
  }

  private static EventRepresentation frontendEventRepresentation(
      String type, String userId, long time, Map<String, String> details) {

    return eventRepresentation(type, FRONTEND_CLIENT_ID, userId, time, details);
  }

  private static EventRepresentation backendEventRepresentation(
      String type, String userId, long time, Map<String, String> details) {

    return eventRepresentation(type, BACKEND_CLIENT_ID, userId, time, details);
  }

  private static EventRepresentation eventRepresentation(
      String type, String clientId, String userId, long time, Map<String, String> details) {

    EventRepresentation event = new EventRepresentation();
    event.setType(type);
    event.setClientId(clientId);
    event.setUserId(userId);
    event.setIpAddress(IP_ADDRESS);
    event.setTime(time);
    event.setDetails(details);
    return event;
  }

  private static EventDto loginEventDto(
      String codeId, String userId, String userName, long timestamp) {

    return eventDto(LOGIN, codeId, userId, userName, timestamp);
  }

  private static EventDto loginErrorEventDto(
      String codeId, String userId, String userName, long timestamp) {

    return eventDto(LOGIN_ERROR, codeId, userId, userName, timestamp);
  }

  private static EventDto extendSessionEventDto(
      String codeId, String userId, long timestamp) {

    return eventDto(EXTEND_SESSION, codeId, userId, null, timestamp);
  }

  private static EventDto logoutEventDto(
      String codeId, String userId, long timestamp) {

    return eventDto(LOGOUT, codeId, userId, null, timestamp);
  }

  private static EventDto eventDto(
      EventType eventType, String codeId, String userId, String userName, long timestamp) {

    return EventDto.builder()
        .type(eventType)
        .codeId(codeId)
        .userId(userId)
        .userName(userName)
        .ipAddress(IP_ADDRESS)
        .timestamp(timestamp)
        .build();
  }
}
