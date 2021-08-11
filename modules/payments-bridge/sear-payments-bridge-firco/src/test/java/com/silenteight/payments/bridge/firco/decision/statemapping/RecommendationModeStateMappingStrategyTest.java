package com.silenteight.payments.bridge.firco.decision.statemapping;

import com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategy.MapStateInput;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.silenteight.payments.bridge.firco.decision.statemapping.StateMappingStrategyFixture.createRecommendationStateMappingStrategy;
import static org.assertj.core.api.Assertions.*;

class RecommendationModeStateMappingStrategyTest {

  private final StateMappingStrategy strategy = createRecommendationStateMappingStrategy();

  @ParameterizedTest
  @CsvFileSource(
      resources = "/com/silenteight/payments/bridge/firco/decision/statemapping"
          + "/RecommendationModeStateMappingStrategyTest.csv",
      delimiter = ',',
      numLinesToSkip = 1)
  void shouldMapSourceToExpectedState(String dataCenter, String sourceState, String expectedState) {
    var request = MapStateInput.builder()
        .unit("UNIT DOES NOT MATTER")
        .recommendedAction("RECOMMENDED ACTION DOES NOT MATTER")
        .dataCenter(dataCenter)
        .sourceState(sourceState)
        .build();

    var response = strategy.mapState(request);

    assertThat(response.getDestinationState()).isEqualTo(expectedState);
  }
}
