package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.time.TimeConverter;

import one.util.streamex.EntryStream;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.LOGIN_EVENT_TYPE;
import static one.util.streamex.StreamEx.of;

@RequiredArgsConstructor
class LastLoginTimeBulkFetcher {

  private final RealmResource realmResource;
  private final TimeConverter timeConverter;

  Map<String, OffsetDateTime> fetch(long limit) {
    if (limit == 0)
      throw new IllegalArgumentException("Limit can't be 0");

    List<EventRepresentation> loginEvents = getAllLoginEvents();

    Map<String, Long> lastLoginTimestampsByUserId = of(loginEvents)
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
        List.of(LOGIN_EVENT_TYPE), null, null, null, null, null, null, null
    );
  }
}
