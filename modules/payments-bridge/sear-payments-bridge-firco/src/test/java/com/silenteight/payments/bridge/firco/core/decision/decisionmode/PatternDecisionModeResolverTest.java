package com.silenteight.payments.bridge.firco.core.decision.decisionmode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class PatternDecisionModeResolverTest {

  private DecisionModeResolver resolver;

  @BeforeEach
  void beforeEach() {
    resolver = DecisionModeResolverFixture.createPatternResolver();
  }

  @Test
  void throwNullPointerExceptionIfParameterIsNull() {
    assertThatThrownBy(() -> resolver.resolve(null)).isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @CsvSource({
      "NBP-KE,RECOMMENDATION",
      "MTS-US,AUTO_DECISION",
      "STARJP,RISK_AUTO_DECISION",
      "MTS-JP,AUTO_DECISION",
      "SOMETHING-JP,RISK_AUTO_DECISION",
      "SOMETHING_ELSE,RECOMMENDATION"
  })
  void resolveAccordingToRequest(String unit, DecisionMode expectedMode) {
    assertThat(resolver.resolve(unit)).isEqualTo(expectedMode);
  }
}
