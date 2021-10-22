package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.*;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.SolutionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

class MatchSolutionFixture {

  static List<SolutionResponse> createSolutionResponses(int solutionCount) {
    return IntStream
        .range(0, solutionCount)
        .mapToObj(i -> SolutionResponse
            .newBuilder()
            .setFeatureVectorSolution(FeatureVectorSolution.FEATURE_VECTOR_SOLUTION_UNSPECIFIED)
            .build())
        .collect(Collectors.toList());
  }

  static SolveMatchesRequest createSolveMatchesRequest(int featureCount) {
    var features = IntStream
        .range(0, featureCount)
        .mapToObj(i -> com.silenteight.solving.api.v1.Feature.newBuilder()
            .setName("feature")
            .build())
        .collect(Collectors.toList());
    return new SolveMatchesRequest(
        1L, "policy/bla", FeatureCollection.newBuilder().addAllFeature(features).build());
  }

  static MatchSolutionCollection createMatchSolutionCollection(int matchSolutionCount) {
    var matchSolutions = new ArrayList<MatchSolution>();
    IntStream.range(0, matchSolutionCount).forEach(n -> matchSolutions.add(createMatchSolution()));
    return new MatchSolutionCollection(1L, matchSolutions);
  }

  static UnsolvedMatchesChunk createUnsolvedMatchesChunk(int count) {
    var unsolvedMatches = LongStream
        .range(0, count)
        .mapToObj(n ->
            UnsolvedMatch.builder()
                .alertId(1L)
                .matchId(n)
                .categories(categories(4))
                .features(features(6))
                .build())
        .collect(toList());

    return new UnsolvedMatchesChunk(unsolvedMatches);
  }

  static List<Category> categories(int count) {
    return IntStream
        .range(0, count)
        .mapToObj(idx -> new Category("categoryLabel" + idx, "category" + idx))
        .collect(toUnmodifiableList());
  }

  static List<Feature> features(int count) {
    return IntStream
        .range(0, count)
        .mapToObj(idx -> Feature.builder()
            .name("featureLabel" + idx)
            .agentConfig("agentConfig" + idx)
            .value("feature" + idx)
            .build())
        .collect(toUnmodifiableList());
  }

  static PolicyAndFeatureVectorElements createAnalysisFeatureVectorElements(int numberOfElements) {
    var elements = IntStream
        .range(0, numberOfElements)
        .mapToObj(n -> "features/bla-" + n)
        .collect(toList())
        .toArray(String[]::new);
    return new PolicyAndFeatureVectorElements("policies/policy", elements, elements);
  }

  private static MatchSolution createMatchSolution() {
    return MatchSolution.builder()
        .alertId(1)
        .matchId(1)
        .response(SolutionResponse.newBuilder()
            .setFeatureVectorSolution(FeatureVectorSolution.SOLUTION_FALSE_POSITIVE)
            .build())
        .categories(categories(4))
        .features(features(6))
        .build();
  }
}
