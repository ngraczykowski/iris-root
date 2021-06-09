package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.engine.analysis.analysis.dto.PolicyAndFeatureVectorElements;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolution;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.MatchSolutionCollection;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;
import com.silenteight.solving.api.v1.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

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
        .mapToObj(i -> Feature.newBuilder().setName("feature").build())
        .collect(Collectors.toList());
    return new SolveMatchesRequest(
        1L, "policy/bla", FeatureCollection.newBuilder().addAllFeature(features).build());
  }

  static MatchSolutionCollection createMatchSolutionCollection(int matchSolutionCount) {
    var matchSolutions = new ArrayList<MatchSolution>();
    IntStream.range(0, matchSolutionCount).forEach(n -> matchSolutions.add(createMatchSolution()));
    return new MatchSolutionCollection(1L, matchSolutions);
  }

  static UnsolvedMatchesChunk createUnsolvedMatchesChunk(int featureVectorCount) {
    long[] matchId = { 1, 2, 3, 4, 5, 6, 7, 8, 8, 10 };
    List<FeatureVector> featureVectors = IntStream
        .range(0, featureVectorCount)
        .mapToObj(n -> FeatureVector
            .newBuilder()
            .addAllFeatureValue(IntStream.range(0, 10).mapToObj(String::valueOf).collect(
                Collectors.toList()))
            .build())
        .collect(toList());
    return new UnsolvedMatchesChunk(matchId, featureVectors);
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
    return new MatchSolution(1L, SolutionResponse.newBuilder().build());
  }
}
