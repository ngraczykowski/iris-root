package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;
import com.silenteight.sep.usermanagement.api.dto.UserDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.AnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
import static com.silenteight.sens.webapp.scb.user.sync.analyst.domain.GnsOrigin.GNS_ORIGIN;
import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class AnalystSynchronizerTest {

  private AnalystSynchronizer underTest;

  @BeforeEach
  void setUp() {
    underTest = new AnalystSynchronizer();
  }

  @Test
  void analystsToAddWhenNoAnalystsAvailable() {
    // given
    List<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(emptyList(), analysts);

    // then
    assertThat(result.getAdded()).isEqualTo(
        asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME));
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToAddWhenOtherAnalystsAvailable() {
    // given
    Collection<UserDto> users = singletonList(
        createAnalystUser(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEqualTo(singletonList(ANALYST_WITH_DISPLAY_NAME));
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void noAnalystsToAddWhenSameAnalystsAvailable() {
    // given
    Collection<UserDto> users = asList(
        createAnalystUser(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
        createAnalystUser(
            ANALYST_WITH_DISPLAY_NAME.getUserName(), ANALYST_WITH_DISPLAY_NAME.getDisplayName()));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToAddAndDeleteWhenDifferentAnalystsAvailable() {
    // given
    String otherLogin = "other-login";
    Collection<UserDto> users = asList(
        createAnalystUser(otherLogin, "other-display-name"),
        createAnalystUser(
            ANALYST_WITH_DISPLAY_NAME.getUserName(), ANALYST_WITH_DISPLAY_NAME.getDisplayName()),
        createUser("no-analyst-role-login", "no-analyst-role-name", "Maker",
            GNS_ORIGIN),
        createUser("no-gns-login", "no-gns-display-name", ANALYST, SENS_ORIGIN));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEqualTo(singletonList(ANALYST_WITHOUT_DISPLAY_NAME));
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEqualTo(singletonList(otherLogin));
  }

  @Test
  void analystsToAddRoleWhenSameUserNameAndMissingAnalystRole() {
    // given
    Collection<UserDto> users = asList(
        createUser(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName(),
            "Maker",
            GNS_ORIGIN),
        createUser("other-login", "other-display-name", "Maker", SENS_ORIGIN));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEqualTo(
        singletonList(ANALYST_WITHOUT_DISPLAY_NAME.getUserName()));
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToUpdateDisplayNameWhenDisplayNameChanged() {
    // given
    Collection<UserDto> users = asList(
        createAnalystUser(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
        createAnalystUser(ANALYST_WITH_DISPLAY_NAME.getUserName(), "other-display-name"));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getRestored()).isEmpty();
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEqualTo(
        singletonList(
            new UpdatedAnalyst(
                ANALYST_WITH_DISPLAY_NAME.getUserName(),
                ANALYST_WITH_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void resultContainsOneAnalystRestored_whenDeletedAnalystIsAvailableAgain() {
    // given
    Collection<UserDto> users = asList(
        createDeletedAnalystUser(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            ANALYST_WITH_DISPLAY_NAME.getDisplayName(),
            parse("2011-12-03T10:15:30+01:00")));
    Collection<Analyst> analysts = singletonList(ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getRestored())
        .isEqualTo(singletonList(ANALYST_WITH_DISPLAY_NAME.getUserName()));
    assertThat(result.getAddedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  private static final UserDto createAnalystUser(String login, String displayName) {
    return createDeletedAnalystUser(login, displayName, null);
  }

  private static final UserDto createDeletedAnalystUser(
      String login, String displayName, OffsetDateTime deletedAt) {

    return createUser(login, displayName, ANALYST, GNS_ORIGIN, deletedAt);
  }

  private static final UserDto createUser(
      String login, String displayName, String role, String origin) {

    return createUser(login, displayName, role, origin, null);
  }

  private static final UserDto createUser(
      String login, String displayName, String role, String origin, OffsetDateTime deletedAt) {

    return UserDto
        .builder()
        .userName(login)
        .displayName(displayName)
        .roles(singletonList(role))
        .origin(origin)
        .deletedAt(deletedAt)
        .build();
  }
}
