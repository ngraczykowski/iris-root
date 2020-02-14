package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.user.sync.analyst.AnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
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
    assertThat(result.getUpdatedRole()).isEmpty();
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
    assertThat(result.getUpdatedRole()).isEmpty();
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
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            ANALYST_WITH_DISPLAY_NAME.getDisplayName()));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getUpdatedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToAddAndDeleteWhenDifferentAnalystsAvailable() {
    // given
    String otherLogin = "other-login";
    Collection<UserDto> users = asList(
        createAnalystUser(
            otherLogin,
            "other-display-name"),
        createAnalystUser(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            ANALYST_WITH_DISPLAY_NAME.getDisplayName()),
        createUser(
            "no-analyst-role-login",
            "other-display-name",
            "Maker"));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEqualTo(singletonList(ANALYST_WITHOUT_DISPLAY_NAME));
    assertThat(result.getUpdatedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEmpty();
    assertThat(result.getDeleted()).isEqualTo(singletonList(otherLogin));
  }

  @Test
  void analystsToUpdateRoleWhenSameUserNameAndMissingAnalystRole() {
    // given
    Collection<UserDto> users = singletonList(
        createUser(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName(),
            "Maker"));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getUpdatedRole()).isEqualTo(
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
        createAnalystUser(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            "other-display-name"));
    Collection<Analyst> analysts = asList(ANALYST_WITHOUT_DISPLAY_NAME, ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(users, analysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getUpdatedRole()).isEmpty();
    assertThat(result.getUpdatedDisplayName()).isEqualTo(
        singletonList(
            new UpdatedAnalyst(
                ANALYST_WITH_DISPLAY_NAME.getUserName(),
                ANALYST_WITH_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getDeleted()).isEmpty();
  }

  private static final UserDto createAnalystUser(String login, String displayName) {
    return createUser(login, displayName, ANALYST);
  }

  private static final UserDto createUser(String login, String displayName, String role) {
    return UserDto
        .builder()
        .userName(login)
        .displayName(displayName)
        .roles(singletonList(role))
        .build();
  }
}
