package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.governance.v1.api.FeatureVector;
import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;
import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;
import com.silenteight.proto.governance.v1.api.SolutionResponse;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class SolveUseCase {

  @NonNull
  private final SolvingService solvingService;

  public GetSolutionsResponse solve(GetSolutionsRequest request) {
    List<String> featureNames = asFeatureNames(request.getFeatureCollection().getFeatureList());
    List<SolutionResponse> solutionResponses = request.getFeatureVectorsList().stream()
        .map(vector -> this.asFeatureValues(featureNames, vector))
        .map(solvingService::solve)
        .map(this::asSolutionResponse)
        .collect(toList());

    return GetSolutionsResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private static List<String> asFeatureNames(
      List<com.silenteight.proto.governance.v1.api.Feature> features) {

    return features
        .stream()
        .map(com.silenteight.proto.governance.v1.api.Feature::getName)
        .collect(toList());
  }

  private Map<String, String> asFeatureValues(
      List<String> featureNames, FeatureVector featureVector) {

    Iterator<String> keyIterator = featureNames.iterator();
    Iterator<String> valueIterator = featureVector.getFeatureValueList().iterator();

    return range(0, featureNames.size())
        .boxed()
        .collect(toMap(i -> keyIterator.next(), i -> valueIterator.next()));
  }

  private SolutionResponse asSolutionResponse(SolveResponse solveResponse) {
    return SolutionResponse.newBuilder()
        .setFeatureVectorSolution(solveResponse.getSolution())
        .setStepId(stepIdAsString(solveResponse.getStepId()))
        // TODO: .setFeatureVectorSignature()
        .build();
  }

  private static String stepIdAsString(UUID stepId) {
    return stepId != null ? stepId.toString() : "";
  }
}
