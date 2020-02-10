package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.time.TimeConverter;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.LOGIN_EVENT_TYPE;

@RequiredArgsConstructor
class SingleRequestLoginTimeProvider implements LastLoginTimeProvider {

  private final RealmResource realmResource;
  private final TimeConverter timeConverter;

  @Override
  public Optional<OffsetDateTime> getForUserId(String userId) {
    List<EventRepresentation> userLoginEvents = getUserLoginEvents(userId);

    return userLoginEvents.stream()
        .max(Comparator.comparingLong(EventRepresentation::getTime))
        .map(EventRepresentation::getTime)
        .map(timeConverter::toOffsetFromSeconds);
  }

  private List<EventRepresentation> getUserLoginEvents(String userId) {
    return realmResource.getEvents(
        List.of(LOGIN_EVENT_TYPE), null, userId, null, null, null, null, null
    );
  }
}
