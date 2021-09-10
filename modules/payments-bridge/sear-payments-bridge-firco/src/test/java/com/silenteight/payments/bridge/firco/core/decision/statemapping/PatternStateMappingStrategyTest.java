package com.silenteight.payments.bridge.firco.core.decision.statemapping;

import com.silenteight.payments.bridge.firco.core.decision.statemapping.StateMappingStrategy.MapStateInput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.*;

class PatternStateMappingStrategyTest {

  private PatternStateMappingStrategy stateMappingStrategy;

  @BeforeEach
  void beforeEach() {
    stateMappingStrategy = StateMappingStrategyFixture.createAutoDecisionStateMappingStrategy();
  }

  @Test
  void throwNullPointerExceptionIfParameterIsNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> stateMappingStrategy.mapState(null));
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/com/silenteight/payments/bridge/firco/core/decision/statemapping"
          + "/AutoDecisioningModeStateMappingStrategyTest_HK.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void shouldMapToExpectedStateForHK(
      String recommendedAction, String unit, String sourceState, String expectedState) {

    runTest("HK", recommendedAction, unit, sourceState, expectedState);
  }

  @ParameterizedTest
  @CsvFileSource(
      resources = "/com/silenteight/payments/bridge/firco/core/decision/statemapping"
          + "/AutoDecisioningModeStateMappingStrategyTest_UK.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void shouldMapToExpectedStateForUK(
      String recommendedAction, String unit, String sourceState, String expectedState) {

    runTest("UK", recommendedAction, unit, sourceState, expectedState);
  }

  private void runTest(
      String country, String recommendedAction, String unit, String sourceState,
      String expectedState) {

    var request = MapStateInput.builder()
        .unit(unit)
        .recommendedAction(recommendedAction)
        .dataCenter(country)
        .sourceState(sourceState)
        .build();

    var response = stateMappingStrategy.mapState(request);

    assertThat(response.getDestinationState()).isEqualTo(expectedState);
  }
}
