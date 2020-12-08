package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.report.domain.ReportMetadataService;
import com.silenteight.sens.webapp.user.roles.UserNotFoundException;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.ConfigurationQuery;
import com.silenteight.sep.usermanagement.api.EventQuery;
import com.silenteight.sep.usermanagement.api.dto.EventDto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.silenteight.sep.usermanagement.api.event.EventType.EXTEND_SESSION;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGOUT;
import static freemarker.template.utility.Collections12.singletonList;
import static java.lang.Math.max;
import static java.lang.Math.toIntExact;
import static java.time.Instant.ofEpochMilli;
import static java.time.OffsetDateTime.ofInstant;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class UserAuthActivityEventProvider {

  private static final String REPORT_NAME = "user-auth-activity";

  private static final int SESSION_TIMEOUT_MILLIS_MARGIN = 5 * 1000;

  @NonNull
  private final ConfigurationQuery configurationQuery;

  @NonNull
  private final EventQuery eventQuery;

  @NonNull
  private final ReportMetadataService reportMetadataService;

  @NonNull
  private final UserRolesRetriever userRolesRetriever;

  @NonNull
  private final ZoneId zoneId;

  List<UserAuthActivityEventDto> provide(
      @NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {

    Map<String, List<EventDto>> eventsStore = new ConcurrentHashMap<>();
    List<UserAuthActivityEventDto> data = convertEventsToUserAuthActivities(from, eventsStore);
    data.addAll(analyzeExpectedLogoutEvents(from, to, eventsStore));
    data.sort(new EventComparator());

    storeNextReportStartTime(from, eventsStore);

    return data;
  }

  private List<UserAuthActivityEventDto> convertEventsToUserAuthActivities(
      OffsetDateTime from, Map<String, List<EventDto>> eventsStore) {

    long fromMillis = from.toInstant().toEpochMilli();
    return getAuthActivityEvents(from)
        .stream()
        .map(event -> convertEventToUserAuthActivities(event, fromMillis, eventsStore))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<EventDto> getAuthActivityEvents(OffsetDateTime from) {
    OffsetDateTime reportFrom = getFromFilterValue(from);
    long reportFromMillis = reportFrom.toInstant().toEpochMilli();

    return eventQuery.getEvents(reportFrom, List.of(LOGIN, LOGOUT, EXTEND_SESSION))
        .stream()
        .filter(e -> e.getTimestamp() >= reportFromMillis)
        .sorted(comparingLong(EventDto::getTimestamp))
        .collect(toList());
  }

  private OffsetDateTime getFromFilterValue(OffsetDateTime from) {
    OffsetDateTime startTime = reportMetadataService.getStartTime(REPORT_NAME);
    return startTime != null ? startTime : from;
  }

  private List<UserAuthActivityEventDto> convertEventToUserAuthActivities(
      EventDto event, long fromMillis, Map<String, List<EventDto>> eventsStore) {

    List<UserAuthActivityEventDto> activities = new ArrayList<>();
    switch (event.getType()) {
      case LOGIN:
        activities.addAll(convertLoginEventToUserAuthActivities(event, fromMillis, eventsStore));
        break;
      case LOGOUT:
        activities.addAll(
            convertLogoutEventToUserAuthActivities(event, fromMillis, eventsStore));
        break;
      case EXTEND_SESSION:
        activities.addAll(
            convertExtendSessionEventToUserAuthActivities(event, eventsStore));
        break;
      default:
        break;
    }

    return activities;
  }

  private List<UserAuthActivityEventDto> convertLoginEventToUserAuthActivities(
      EventDto event, long fromMillis, Map<String, List<EventDto>> eventsStore) {

    List<UserAuthActivityEventDto> activities = new ArrayList<>();
    if (event.getUserId() == null) {
      eventsStore
          .values()
          .stream()
          .filter(events -> isLoginEventForTheSameUser(event.getCodeId(), events))
          .findFirst()
          .ifPresent(events -> events.add(supplyEventUserData(event, events)));
    } else if (eventsStore.containsKey(event.getUserId())) {
      eventsStore.get(event.getUserId()).add(event);
    } else {
      eventsStore.put(event.getUserId(), new ArrayList<>(singletonList(event)));

      if (isEventWithinCurrentReport(event, fromMillis))
        activities.add(mapToUserLoginActivityEventDto(event));
    }

    return activities;
  }

  private List<UserAuthActivityEventDto> convertLogoutEventToUserAuthActivities(
      EventDto event, long fromMillis, Map<String, List<EventDto>> eventsStore) {

    List<UserAuthActivityEventDto> activities = new ArrayList<>();
    if (eventsStore.containsKey(event.getUserId())) {
      List<EventDto> loginEvents = eventsStore.remove(event.getUserId());

      if (isEventWithinCurrentReport(event, fromMillis)) {
        EventDto firstEvent = loginEvents.get(0);

        if (isEventOutsideCurrentReport(firstEvent, fromMillis))
          activities.add(mapToUserLoginActivityEventDto(firstEvent));

        activities.add(mapToUserLogoutActivityEventDto(firstEvent, event.getTimestamp()));
      }
    }

    return activities;
  }

  private static List<UserAuthActivityEventDto> convertExtendSessionEventToUserAuthActivities(
      EventDto event, Map<String, List<EventDto>> eventsStore) {

    List<UserAuthActivityEventDto> activities = new ArrayList<>();
    if (eventsStore.containsKey(event.getUserId())) {
      List<EventDto> events = eventsStore.get(event.getUserId());
      events.add(supplyEventUserData(event, events));
    }

    return activities;
  }

  private static boolean isLoginEventForTheSameUser(String codeId, List<EventDto> events) {
    return events
        .stream()
        .anyMatch(e -> isLoginEventForTheSameUser(codeId, e));
  }

  private static boolean isLoginEventForTheSameUser(String codeId, EventDto event) {
    return event.getType() == LOGIN && event.getCodeId().equals(codeId);
  }

  private static EventDto supplyEventUserData(EventDto event, List<EventDto> previousEvents) {
    EventDto firstEvent = previousEvents.get(0);
    event.setUserId(firstEvent.getUserId());
    event.setUserName(firstEvent.getUserName());

    return event;
  }

  private static boolean isEventWithinCurrentReport(EventDto event, long fromMillis) {
    return event.getTimestamp() >= fromMillis;
  }

  private static boolean isEventOutsideCurrentReport(EventDto event, long fromMillis) {
    return !isEventWithinCurrentReport(event, fromMillis);
  }

  private UserAuthActivityEventDto mapToUserLoginActivityEventDto(EventDto event) {
    return UserAuthActivityEventDto.builder()
        .username(event.getUserName())
        .roles(getUserRoles(event.getUserName()))
        .ipAddress(event.getIpAddress())
        .loginTimestamp(event.getTimestamp())
        .build();
  }

  private UserAuthActivityEventDto mapToUserLogoutActivityEventDto(EventDto event, long timestamp) {
    return UserAuthActivityEventDto.builder()
        .username(event.getUserName())
        .roles(getUserRoles(event.getUserName()))
        .ipAddress(event.getIpAddress())
        .logoutTimestamp(timestamp)
        .build();
  }

  private List<String> getUserRoles(String userName) {
    try {
      return userRolesRetriever.rolesOf(userName);
    } catch (UserNotFoundException e) {
      log.error(e.getMessage(), e);
      return emptyList();
    }
  }

  private List<UserAuthActivityEventDto> analyzeExpectedLogoutEvents(
      OffsetDateTime from, OffsetDateTime to, Map<String, List<EventDto>> eventsStore) {

    long fromMillis = from.toInstant().toEpochMilli();
    long toMillis = to.toInstant().toEpochMilli();
    long sessionTimeoutMillis = configurationQuery.getSessionIdleTimeoutSeconds() * 1000L;

    return eventsStore
        .values()
        .stream()
        .map(events -> analyzeExpectedLogoutEvents(
            events, fromMillis, toMillis, sessionTimeoutMillis, eventsStore))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<UserAuthActivityEventDto> analyzeExpectedLogoutEvents(
      List<EventDto> events,
      long fromMillis,
      long toMillis,
      long sessionTimeoutMillis,
      Map<String, List<EventDto>> eventsStore) {

    List<UserAuthActivityEventDto> activities = new ArrayList<>();
    EventDto lastEvent = events.get(events.size() - 1);

    if (shouldEventBeTreatedAsExpiredLogin(
        lastEvent, fromMillis, toMillis, sessionTimeoutMillis)) {
      EventDto firstEvent = events.get(0);
      if (isEventOutsideCurrentReport(firstEvent, fromMillis))
        activities.add(mapToUserLoginActivityEventDto(firstEvent));

      activities.add(mapToUserLogoutActivityEventDto(
          lastEvent, lastEvent.getTimestamp() + sessionTimeoutMillis));

      eventsStore.remove(firstEvent.getUserId());
    }

    return activities;
  }

  private static boolean shouldEventBeTreatedAsExpiredLogin(
      EventDto event, long fromMillis, long toMillis, long sessionTimeoutMillis) {

    return toMillis - event.getTimestamp()
          > sessionTimeoutMillis + SESSION_TIMEOUT_MILLIS_MARGIN
        && event.getTimestamp() + sessionTimeoutMillis >= fromMillis;
  }

  private void storeNextReportStartTime(
      OffsetDateTime from, Map<String, List<EventDto>> eventsStore) {

    OffsetDateTime nextReportStartTime = eventsStore
        .values()
        .stream()
        .flatMap(List::stream)
        .sorted(comparingLong(EventDto::getTimestamp))
        .findFirst()
        .map(event -> ofInstant(ofEpochMilli(event.getTimestamp()), zoneId))
        .orElse(from);
    reportMetadataService.saveStartTime(REPORT_NAME, nextReportStartTime);
  }

  private static class EventComparator
      implements Comparator<UserAuthActivityEventDto>, Serializable {

    private static final long serialVersionUID = 5204798584078963018L;

    @Override
    public int compare(UserAuthActivityEventDto dto1, UserAuthActivityEventDto dto2) {
      return toIntExact(
          max(dto1.getLoginTimestamp(), dto1.getLogoutTimestamp())
              - max(dto2.getLoginTimestamp(), dto2.getLogoutTimestamp()));
    }
  }
}
