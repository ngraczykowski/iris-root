package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.governance.v1.api.FeatureVector;
import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;
import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;
import com.silenteight.proto.governance.v1.api.SolutionResponse;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SolveUseCase {

  @NonNull
  private final SolvingService solvingService;

  public GetSolutionsResponse solve(GetSolutionsRequest request) {
    List<SolutionResponse> solutionResponses = request.getFeatureVectorsList().stream()
        .map(this::asListOfValues)
        .map(solvingService::getSolution)
        .map(this::asSolutionResponse)
        .collect(toList());

    return GetSolutionsResponse.newBuilder()
        .addAllSolutions(solutionResponses)
        .build();
  }

  private List<String> asListOfValues(FeatureVector featureVector) {
    return new ArrayList<>(featureVector.getFeatureValueList());
  }

  private SolutionResponse asSolutionResponse(SolutionWithStepId solutionWithStepId) {
    return SolutionResponse.newBuilder()
        .setFeatureVectorSolution(solutionWithStepId.getSolution())
        .setStepId(solutionWithStepId.getStepId())
        // TODO: .setFeatureVectorSignature()
        .build();
  }
}
