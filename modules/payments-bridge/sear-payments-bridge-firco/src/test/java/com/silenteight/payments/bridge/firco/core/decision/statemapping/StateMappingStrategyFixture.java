package com.silenteight.payments.bridge.firco.core.decision.statemapping;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StateMappingStrategyFixture {

  static PatternStateMappingStrategy createAutoDecisionStateMappingStrategy() {
    return getStrategy(
        "autoDecisionModeStrategy",
        "decision/state-mapping-strategy/auto-decision.csv");
  }

  static PatternStateMappingStrategy createRecommendationStateMappingStrategy() {
    return getStrategy(
        "recommendationModeStrategy",
        "decision/state-mapping-strategy/recommendation.csv");
  }

  private static PatternStateMappingStrategy getStrategy(
      String strategyName, String resourceName) {

    try (var reader = new InputStreamReader(getResourceAsStream(resourceName))) {
      var patternTuples = new CsvPatternTupleLoader().load(reader);
      return new PatternStateMappingStrategy(strategyName, patternTuples);
    } catch (Exception e) {
      return rethrow(e);
    }
  }

  private static InputStream getResourceAsStream(String resourceName) {
    return StateMappingStrategyFixture.class.getClassLoader().getResourceAsStream(resourceName);
  }
}
