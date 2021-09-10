package com.silenteight.payments.bridge.firco.core.decision.decisionmode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DecisionModeResolverFixture {

  public static PatternDecisionModeResolver createPatternResolver() {
    try (var reader = new InputStreamReader(getResourceAsStream())) {
      return new CsvPatternDecisionModeResolverFactory().create(reader);
    } catch (Exception e) {
      return rethrow(e);
    }
  }

  @Nullable
  private static InputStream getResourceAsStream() {
    return DecisionModeResolverFixture.class.getClassLoader().getResourceAsStream(
        "decision/decision-mode/decision-mode-by-unit.csv");
  }
}
