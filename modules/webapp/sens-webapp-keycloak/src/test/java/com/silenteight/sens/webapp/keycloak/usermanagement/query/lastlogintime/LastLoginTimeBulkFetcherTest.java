package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.common.time.TimeConverter;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.EventRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.KeycloakLastLoginTimeConfiguration.LOGIN_EVENT_TYPE;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeBulkFetcherTest.LastLoginTimeBulkFetcherFixtures.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LastLoginTimeBulkFetcherTest {

  public static final TimeConverter TIME_CONVERTER =
      new TimeConverter(MockTimeSource.ARBITRARY_INSTANCE);

  @Mock
  private RealmResource realmResource;

  private LastLoginTimeBulkFetcher underTest;

  @BeforeEach
  void setUp() {
    underTest = new LastLoginTimeBulkFetcher(
        realmResource,
        TIME_CONVERTER);
  }

  @Test
  void limit0_illegalArgumentException() {
    ThrowingCallable when = () -> underTest.fetch(0);

    assertThatThrownBy(when).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void limitNegative_illegalArgumentException() {
    ThrowingCallable when = () -> underTest.fetch(-5);

    assertThatThrownBy(when).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void emptyMap_noEventsAndFetchingWithLimitOf15() {
    givenNoEvents();

    Map<String, OffsetDateTime> actual = underTest.fetch(15);

    assertThat(actual).isEmpty();
  }

  private void givenEvents(List<KeycloakLoginEvent> events) {
    List<EventRepresentation> representations =
        events.stream()
            .map(KeycloakLoginEvent::getRepresentation).collect(toList());

    given(realmResource.getEvents(
        of(LOGIN_EVENT_TYPE),
        null, null, null, null, null, null, null))
        .willReturn(representations);
  }

  private void givenNoEvents() {
    givenEvents(emptyList());
  }

  @Nested
  class GivenSixFixtureEvents {

    @BeforeEach
    void setUp() {
      givenEvents(List.of(EVENT_1, EVENT_2, EVENT_3, EVENT_4, EVENT_5, EVENT_6));
    }

    @Test
    void limit2_returnsLastLoginTimesForTwoLastlySignedInUsers() {
      Map<String, OffsetDateTime> actual = underTest.fetch(2);

      assertThat(actual)
          .containsOnly(
              entry(USER_3_ID, EVENT_5.getTime()),
              entry(USER_2_ID, EVENT_6.getTime()));
    }

    @Test
    void limit3_returnsLastLoginTimesForThreeLastlySignedInUsers() {
      Map<String, OffsetDateTime> actual = underTest.fetch(3);

      assertThat(actual)
          .containsOnly(
              entry(USER_3_ID, EVENT_5.getTime()),
              entry(USER_2_ID, EVENT_6.getTime()),
              entry(USER_1_ID, EVENT_4.getTime()));
    }

    @Test
    void limit1_returnsLastLoginTimeForLastlySignedInUser() {
      Map<String, OffsetDateTime> actual = underTest.fetch(1);

      assertThat(actual)
          .containsOnly(
              entry(USER_2_ID, EVENT_6.getTime()));
    }
  }

  @Nested
  class GivenFixtureEventWithoutUserId {

    @BeforeEach
    void setUp() {
      givenEvents(singletonList(EVENT_WITHOUT_USER_ID));
    }

    @Test
    void limit1_returnsEmptyLastLoginTimes() {
      Map<String, OffsetDateTime> actual = underTest.fetch(1);

      assertThat(actual).isEmpty();
    }
  }

  static class LastLoginTimeBulkFetcherFixtures {

    static final String USER_1_ID = "6d521a7c-8066-4e10-bec4-d33f7a0191ae";
    static final String USER_2_ID = "d2262382-ea61-4ffc-a459-0e20557e8dba";
    static final String USER_3_ID = "30da08c2-6fcc-4350-8ba1-a5ba7798b857";

    static final KeycloakLoginEvent EVENT_1 = KeycloakLoginEvent.builder()
        .time("2020-02-01T10:10:10Z").userId(USER_1_ID).build();
    static final KeycloakLoginEvent EVENT_2 = KeycloakLoginEvent.builder()
        .time("2020-02-02T10:10:10Z").userId(USER_2_ID).build();
    static final KeycloakLoginEvent EVENT_3 = KeycloakLoginEvent.builder()
        .time("2020-02-03T10:10:10Z").userId(USER_2_ID).build();
    static final KeycloakLoginEvent EVENT_4 = KeycloakLoginEvent.builder()
        .time("2020-02-04T10:10:10Z").userId(USER_1_ID).build();
    static final KeycloakLoginEvent EVENT_5 = KeycloakLoginEvent.builder()
        .time("2020-02-05T10:10:10Z").userId(USER_3_ID).build();
    static final KeycloakLoginEvent EVENT_6 = KeycloakLoginEvent.builder()
        .time("2020-02-06T10:10:10Z").userId(USER_2_ID).build();
    static final KeycloakLoginEvent EVENT_WITHOUT_USER_ID = KeycloakLoginEvent.builder()
        .time("2020-02-07T10:10:10Z").build();
  }
}
