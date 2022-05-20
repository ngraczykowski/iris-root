package com.silenteight.payments.bridge.firco.decision.decisionmode;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Slf4j
class PatternDecisionModeResolver implements DecisionModeResolver {

  private final List<UnitPatternDecisionTuple> unitPatterns;
  private final DecisionMode defaultDecisionMode;
  private final PathMatcher matcher = new AntPathMatcher();

  @Override
  @Nonnull
  public DecisionMode resolve(@NonNull String unit) {
    return unitPatterns.stream()
        .filter(tuple -> matcher.match(tuple.getUnitPattern(), unit))
        .findAny()
        .map(tuple -> {
          var mode = tuple.getMode();
          log.debug("Resolved recommendation mode [{}] for unit [{}]", mode, unit);
          return mode;
        })
        .orElseGet(() -> {
          log.debug("Resolved default recommendation mode [{}] for unit [{}]",
              defaultDecisionMode, unit);
          return defaultDecisionMode;
        });
  }
}
