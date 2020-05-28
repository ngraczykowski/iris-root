package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.experimental.UtilityClass;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLoginEvent.builder;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.SingleRequestLoginTimeProviderTest.SingleRequestLoginTimeProviderFixtures.EARLIER_EVENT;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.SingleRequestLoginTimeProviderTest.SingleRequestLoginTimeProviderFixtures.LATER_EVENT;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.SingleRequestLoginTimeProviderTest.SingleRequestLoginTimeProviderFixtures.USER_ID;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SingleRequestLoginTimeProviderTest {

  static final String LOGIN_EVENT_TYPE = "LOGIN";

  private SingleRequestLoginTimeProvider underTest;

  @Mock
  private RealmResource realmResource;

  @BeforeEach
  void setUp() {
    underTest = new SingleRequestLoginTimeProvider(
        realmResource, new TimeConverter(MockTimeSource.ARBITRARY_INSTANCE));
  }

  @Test
  void returnsEmptyOptional_whenNoEvents() {
    Optional<OffsetDateTime> actual = underTest.getForUserId(USER_ID);

    assertThat(actual).isEmpty();
  }

  @Test
  void returnsCorrectTime_whenOnlyOneEvent() {
    givenUserLoginEvents(of(EARLIER_EVENT));

    Optional<OffsetDateTime> actual = underTest.getForUserId(USER_ID);

    assertThat(actual).contains(EARLIER_EVENT.getTime());
  }

  @Test
  void returnsEventWithEarlierTimestamp_whenTwoEvents() {
    givenUserLoginEvents(of(EARLIER_EVENT, LATER_EVENT));

    Optional<OffsetDateTime> actual = underTest.getForUserId(USER_ID);

    assertThat(actual).contains(EARLIER_EVENT.getTime());
  }

  private void givenUserLoginEvents(List<KeycloakLoginEvent> events) {
    List<EventRepresentation> representations =
        events.stream().map(KeycloakLoginEvent::getRepresentation).collect(toList());

    given(
        realmResource.getEvents(of(LOGIN_EVENT_TYPE), null, USER_ID, null, null, null, null, null))
        .willReturn(representations);
  }

  @UtilityClass
  static class SingleRequestLoginTimeProviderFixtures {

    static final String USER_ID = "98fa3f8c-c885-4390-8226-4546532064f8";

    static final KeycloakLoginEvent EARLIER_EVENT =
        builder().time("2020-02-02T10:10:10Z").build();

    static final KeycloakLoginEvent LATER_EVENT =
        builder().time("2020-02-01T10:10:10Z").build();
  }
}
