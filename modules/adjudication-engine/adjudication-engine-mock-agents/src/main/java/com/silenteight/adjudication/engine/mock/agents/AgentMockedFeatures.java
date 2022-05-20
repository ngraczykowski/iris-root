package com.silenteight.adjudication.engine.mock.agents;

import lombok.NoArgsConstructor;

import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import java.util.List;
import java.util.regex.Pattern;

import static com.silenteight.adjudication.engine.mock.agents.JsonReasonReader.readFromResource;
import static java.util.regex.Pattern.compile;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class AgentMockedFeatures {

  private static final Pattern GEO_FEATURE_PATTERN = compile("^.*geo.*$");
  private static final Pattern DOB_FEATURE_PATTERN = compile("^.*dateOfBirth$");

  private static final List<FeatureSolutionProvider> PROVIDERS = List.of(
      FeatureSolutionProvider.builder()
          .pattern(GEO_FEATURE_PATTERN)
          .solution(readSolution("CITY_NO_MATCH", "reasons/geo_city_no_match.json"))
          .solution(readSolution("CITY_MATCH", "reasons/geo_city_match.json"))
          .build(),
      FeatureSolutionProvider.builder()
          .pattern(DOB_FEATURE_PATTERN)
          .solution(readSolution("EXACT", "reasons/date_of_birth_exact.json"))
          .solution(readSolution("OUT_OF_RANGE", "reasons/date_of_birth_out_of_range.json"))
          .build());

  static Feature getRandomFeature(String featureName) {
    return Feature.newBuilder()
        .setFeature(featureName)
        .setFeatureSolution(getSolutionFor(featureName))
        .build();
  }

  private static FeatureSolution readSolution(String solution, String resource) {
    return FeatureSolution.newBuilder()
        .setSolution(solution)
        .setReason(readFromResource(resource))
        .build();
  }

  private static FeatureSolution getSolutionFor(String feature) {
    for (var provider : PROVIDERS) {
      if (provider.matches(feature)) {
        return provider.getRandom();
      }
    }

    return FeatureSolution.newBuilder()
        .setSolution("INCONCLUSIVE")
        .build();
  }
}
