package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.NewAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.SynchronizedAnalysts;
import com.silenteight.sens.webapp.user.sync.analyst.AnalystSynchronizer.UpdatedAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;
import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.sens.webapp.user.sync.analyst.ExternalAnalystFixtures.ANALYST_WITHOUT_DISPLAY_NAME;
import static com.silenteight.sens.webapp.user.sync.analyst.ExternalAnalystFixtures.ANALYST_WITH_DISPLAY_NAME;
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
    List<ExternalAnalyst> externalAnalysts = asList(
        ANALYST_WITHOUT_DISPLAY_NAME,
        ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(emptyList(), externalAnalysts);

    // then
    assertThat(result.getAdded()).isEqualTo(
        asList(
            new NewAnalyst(
                ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
                ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
            new NewAnalyst(
                ANALYST_WITH_DISPLAY_NAME.getUserName(),
                ANALYST_WITH_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getUpdated()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToAddWhenOtherAnalystsAvailable() {
    // given
    List<InternalAnalyst> internalAnalysts = singletonList(
        createInternalAnalyst(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()));
    List<ExternalAnalyst> externalAnalysts = asList(
        ANALYST_WITHOUT_DISPLAY_NAME,
        ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(internalAnalysts, externalAnalysts);

    // then
    assertThat(result.getAdded()).isEqualTo(
        singletonList(
            new NewAnalyst(
                ANALYST_WITH_DISPLAY_NAME.getUserName(),
                ANALYST_WITH_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getUpdated()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void noAnalystsToAddWhenSameAnalystsAvailable() {
    // given
    List<InternalAnalyst> internalAnalysts = asList(
        createInternalAnalyst(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
        createInternalAnalyst(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            ANALYST_WITH_DISPLAY_NAME.getDisplayName()));
    List<ExternalAnalyst> externalAnalysts = asList(
        ANALYST_WITHOUT_DISPLAY_NAME,
        ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(internalAnalysts, externalAnalysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getUpdated()).isEmpty();
    assertThat(result.getDeleted()).isEmpty();
  }

  @Test
  void analystsToAddAndDeleteWhenDifferentAnalystsAvailable() {
    // given
    String otherLogin = "other-login";
    List<InternalAnalyst> internalAnalysts = asList(
        createInternalAnalyst(
            otherLogin,
            "other-display-name"),
        createInternalAnalyst(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            ANALYST_WITH_DISPLAY_NAME.getDisplayName()));
    List<ExternalAnalyst> externalAnalysts = asList(
        ANALYST_WITHOUT_DISPLAY_NAME,
        ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(internalAnalysts, externalAnalysts);

    // then
    assertThat(result.getAdded()).isEqualTo(
        singletonList(
            new NewAnalyst(
                ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
                ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getUpdated()).isEmpty();
    assertThat(result.getDeleted()).isEqualTo(singletonList(otherLogin));
  }

  @Test
  void analystsToUpdateWhenDisplayNameChanged() {
    // given
    List<InternalAnalyst> internalAnalysts = asList(
        createInternalAnalyst(
            ANALYST_WITHOUT_DISPLAY_NAME.getUserName(),
            ANALYST_WITHOUT_DISPLAY_NAME.getDisplayName()),
        createInternalAnalyst(
            ANALYST_WITH_DISPLAY_NAME.getUserName(),
            "other-display-name"));
    List<ExternalAnalyst> externalAnalysts = asList(
        ANALYST_WITHOUT_DISPLAY_NAME,
        ANALYST_WITH_DISPLAY_NAME);

    // when
    SynchronizedAnalysts result = underTest.synchronize(internalAnalysts, externalAnalysts);

    // then
    assertThat(result.getAdded()).isEmpty();
    assertThat(result.getUpdated()).isEqualTo(
        singletonList(
            new UpdatedAnalyst(
                ANALYST_WITH_DISPLAY_NAME.getUserName(),
                ANALYST_WITH_DISPLAY_NAME.getDisplayName())));
    assertThat(result.getDeleted()).isEmpty();
  }

  private static final InternalAnalyst createInternalAnalyst(String login, String displayName) {
    return InternalAnalyst
        .builder()
        .userName(login)
        .displayName(displayName)
        .build();
  }
}
