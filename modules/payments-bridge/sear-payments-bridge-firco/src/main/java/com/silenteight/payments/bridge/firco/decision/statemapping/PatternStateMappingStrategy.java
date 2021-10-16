package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
class PatternStateMappingStrategy implements StateMappingStrategy {

  @Getter
  @NonNull
  private final String name;

  @NonNull
  private final List<PatternTuple> patterns;

  private final PathMatcher matcher = new AntPathMatcher();

  @NonNull
  @Override
  public MapStateOutput mapState(@NonNull MapStateInput request) {
    return mapState(request, () -> {
      throw new CouldNotMapStateException(getName(), request);
    });
  }

  @NonNull
  @Override
  public MapStateOutput mapState(
      @NonNull MapStateInput request,
      Supplier<? extends MapStateOutput> orElseHandler) {

    return patterns.stream()
        .filter(patternTuple -> matches(request, patternTuple))
        .findAny()
        .map(patternTuple -> {
          var mapStateOutput = asMapStateOutput(patternTuple);
          log.debug("Mapped input [{}] to output [{}]", request, mapStateOutput);
          return mapStateOutput;
        })
        .orElseGet(orElseHandler);
  }

  @SuppressWarnings("FeatureEnvy")
  private boolean matches(MapStateInput request, PatternTuple pattern) {
    return matcher.match(pattern.getDataCenter(), request.getDataCenter()) &&
        matcher.match(pattern.getSourceState(), request.getSourceState()) &&
        matcher.match(pattern.getUnit(), request.getUnit()) &&
        matcher.match(pattern.getRecommendation(), request.getRecommendedAction());
  }

  private static MapStateOutput asMapStateOutput(PatternTuple pattern) {
    return new MapStateOutput(pattern.getDestinationState());
  }

  public static class CouldNotMapStateException extends RuntimeException {

    private static final long serialVersionUID = 872963616208335082L;

    public CouldNotMapStateException(String strategyName, MapStateInput input) {
      super("Could not map alert input to output. You probably need to fix"
          + " configuration of " + strategyName + ". Input: " + input);
    }
  }
}
