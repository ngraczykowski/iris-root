package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.event.EventQuery;
import com.silenteight.sep.usermanagement.api.event.EventType;
import com.silenteight.sep.usermanagement.api.event.dto.EventDto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Predicate;

import static com.silenteight.sep.usermanagement.api.event.EventType.EXTEND_SESSION;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN_ERROR;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGOUT;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
class KeycloakEventQuery implements EventQuery {

  private static final String LOGIN_EVENT_TYPE = "LOGIN";
  private static final String LOGIN_ERROR_EVENT_TYPE = "LOGIN_ERROR";
  private static final String LOGOUT_EVENT_TYPE = "LOGOUT";
  private static final String EXTEND_SESSION_EVENT_TYPE = "REFRESH_TOKEN";

  private static final Map<EventType, Set<String>> EVENT_TYPES_MAPPING = Map.of(
      LOGIN, Set.of(LOGIN_EVENT_TYPE),
      LOGIN_ERROR, Set.of(LOGIN_ERROR_EVENT_TYPE),
      LOGOUT, Set.of(LOGOUT_EVENT_TYPE),
      EXTEND_SESSION, Set.of(EXTEND_SESSION_EVENT_TYPE));

  private static final String CODE_ID_DETAILS = "code_id";
  private static final String USERNAME_DETAILS = "username";
  private static final String FRONTEND_CLIENT_ID = "frontend";

  private final RealmResource realmResource;

  @Override
  public List<EventDto> list(OffsetDateTime from, Collection<EventType> types) {
    List<EventRepresentation> events = getEvents(
        mapToKeycloakEventTypes(types), createDateFromFilterValue(from));

    List<EventDto> result = new ArrayList<>();
    types.forEach(type -> {
      if (type == LOGIN)
        result.addAll(getLoginEvents(events));
      if (type == LOGIN_ERROR)
        result.addAll(getLoginErrorEvents(events));
      if (type == LOGOUT)
        result.addAll(getLogoutEvents(events));
      if (type == EXTEND_SESSION)
        result.addAll(getRefreshTokenEvents(events));
    });

    return result;
  }

  private static List<String> mapToKeycloakEventTypes(Collection<EventType> types) {
    return types
        .stream()
        .map(EVENT_TYPES_MAPPING::get)
        .flatMap(Set::stream)
        .collect(toList());
  }

  private List<EventRepresentation> getEvents(List<String> types, String from) {
    return realmResource.getEvents(types, null, null, from, null, null, null, null);
  }

  private List<EventDto> getLoginEvents(List<EventRepresentation> events) {
    return getEventsForType(events, LOGIN);
  }

  private List<EventDto> getLoginErrorEvents(List<EventRepresentation> events) {
    return getEventsForType(events, LOGIN_ERROR, KeycloakEventQuery::isUsernamePresent);
  }

  private List<EventDto> getLogoutEvents(List<EventRepresentation> events) {
    return getEventsForType(events, LOGOUT);
  }

  private List<EventDto> getRefreshTokenEvents(List<EventRepresentation> events) {
    return getEventsForType(events, EXTEND_SESSION, KeycloakEventQuery::isFrontendEvent);
  }

  @NotNull
  private List<EventDto> getEventsForType(List<EventRepresentation> events, EventType eventType) {
    return getEventsForType(events, eventType, eventRepresentation -> true);
  }

  @NotNull
  private List<EventDto> getEventsForType(
      List<EventRepresentation> events, EventType eventType,
      Predicate<EventRepresentation> additionalFilter) {

    return events
        .stream()
        .filter(event -> EVENT_TYPES_MAPPING.get(eventType).contains(event.getType()))
        .filter(additionalFilter)
        .map(e -> mapToDto(e, eventType))
        .collect(toList());
  }

  private static String createDateFromFilterValue(OffsetDateTime dateTime) {
    return dateTime.format(ISO_DATE);
  }

  private static EventDto mapToDto(EventRepresentation event, EventType eventType) {
    return EventDto.builder()
        .type(eventType)
        .codeId(event.getDetails().get(CODE_ID_DETAILS))
        .userId(event.getUserId())
        .userName(event.getDetails().get(USERNAME_DETAILS))
        .ipAddress(event.getIpAddress())
        .timestamp(event.getTime())
        .build();
  }

  private static boolean isUsernamePresent(EventRepresentation event) {
    return isNotBlank(event.getDetails().get(USERNAME_DETAILS));
  }

  private static boolean isFrontendEvent(EventRepresentation event) {
    return StringUtils.equals(event.getClientId(), FRONTEND_CLIENT_ID);
  }
}
