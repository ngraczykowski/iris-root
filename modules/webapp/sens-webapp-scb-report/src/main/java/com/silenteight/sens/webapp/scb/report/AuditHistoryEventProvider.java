package com.silenteight.sens.webapp.scb.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.EventQuery;
import com.silenteight.sep.usermanagement.api.dto.EventDto;
import com.silenteight.sep.usermanagement.api.event.EventType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN;
import static com.silenteight.sep.usermanagement.api.event.EventType.LOGIN_ERROR;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AuditHistoryEventProvider {

  private static final String SUCCESS_STATUS = "SUCCESS";
  private static final String FAILURE_STATUS = "FAILED";

  private static final Map<EventType, String> EVENT_TYPES_MAPPING = Map.of(
      LOGIN, SUCCESS_STATUS,
      LOGIN_ERROR, FAILURE_STATUS);

  @NonNull
  private final EventQuery eventQuery;

  List<AuditHistoryEventDto> provide(@NonNull OffsetDateTime from, @NonNull OffsetDateTime to) {
    long reportFromMillis = from.toInstant().toEpochMilli();
    return eventQuery.getEvents(from, List.of(LOGIN, LOGIN_ERROR))
        .stream()
        .filter(e -> e.getTimestamp() >= reportFromMillis)
        .sorted(comparingLong(EventDto::getTimestamp))
        .map(AuditHistoryEventProvider::mapToDto)
        .collect(toList());
  }

  private static AuditHistoryEventDto mapToDto(EventDto event) {
    return AuditHistoryEventDto.builder()
        .username(event.getUserName())
        .status(EVENT_TYPES_MAPPING.get(event.getType()))
        .ipAddress(event.getIpAddress())
        .timestamp(event.getTimestamp())
        .build();
  }
}
