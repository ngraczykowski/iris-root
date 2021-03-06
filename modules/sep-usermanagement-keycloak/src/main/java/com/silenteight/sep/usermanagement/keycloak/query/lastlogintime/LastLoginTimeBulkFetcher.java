package com.silenteight.sep.usermanagement.keycloak.query.lastlogintime;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeConverter;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RequiredArgsConstructor
class LastLoginTimeBulkFetcher {

  private final RealmResource realmResource;
  private final TimeConverter timeConverter;

  Map<String, OffsetDateTime> fetch(long limit) {
    if (limit <= 0)
      throw new IllegalArgumentException("Limit must be positive");

    List<EventRepresentation> loginEvents = getAllLoginEvents();

    Map<String, Long> lastLoginTimestampsByUserId = StreamEx.of(loginEvents)
        .filter(event -> event.getUserId() != null)
        .reverseSorted(Comparator.comparingLong(EventRepresentation::getTime))
        .distinct(EventRepresentation::getUserId)
        .toMap(EventRepresentation::getUserId, EventRepresentation::getTime, Long::max);

    return EntryStream.of(lastLoginTimestampsByUserId)
        .reverseSorted(Entry.comparingByValue())
        .limit(limit)
        .mapValues(timeConverter::toOffsetFromMilli)
        .toMap();
  }

  private List<EventRepresentation> getAllLoginEvents() {
    return realmResource.getEvents(
        List.of(KeycloakLastLoginTimeConfiguration.LOGIN_EVENT_TYPE), null, null, null, null, null,
        null, null);
  }
}
