package com.silenteight.serp.governance.qa.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.qa.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.domain.DecisionState.PASSED;
import static com.silenteight.serp.governance.qa.domain.DecisionState.VIEWING;
import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.*;

class DecisionTest {

  private static final OffsetDateTime NOW = parse("2021-06-16T08:09:25.481564Z");
  Decision underTest = new Decision();

  @Test
  void restartViewingStateShouldSetNewStateWhenInViewingState() {
    underTest.setState(VIEWING);
    underTest.setUpdatedAt(NOW);

    underTest.resetViewingState();

    Assertions.assertThat(underTest.getState()).isEqualTo(NEW);
    Assertions.assertThat(underTest.getUpdatedAt()).isAfter(NOW);
  }

  @Test
  void restartViewingStateShouldNotUpdateStateWhenInFailedState() {
    underTest.setState(FAILED);
    underTest.setUpdatedAt(NOW);

    underTest.resetViewingState();

    assertThat(underTest.getState()).isEqualTo(FAILED);
    assertThat(underTest.getUpdatedAt()).isEqualTo(NOW);
  }

  @ParameterizedTest
  @MethodSource("provideDecisionStatesForCanBeProcessed")
  void canBeProcessedShouldReturnCorrectBooleanValue(DecisionState state, boolean expected) {
    underTest.setState(state);
    assertThat(underTest.canBeProcessed()).isEqualTo(expected);
  }

  private static Stream<Arguments> provideDecisionStatesForCanBeProcessed() {
    return Stream.of(
        Arguments.of(NEW, true),
        Arguments.of(VIEWING, true),
        Arguments.of(PASSED, false),
        Arguments.of(FAILED, false)
    );
  }
}
