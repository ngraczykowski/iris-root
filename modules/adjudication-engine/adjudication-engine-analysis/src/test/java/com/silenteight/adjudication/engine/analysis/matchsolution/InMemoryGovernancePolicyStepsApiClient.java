package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.SolutionResponse;

import java.util.ArrayList;

public class InMemoryGovernancePolicyStepsApiClient extends GovernancePolicyStepsApiClient {

  private int solvedFeatureVectors = 0;

  public InMemoryGovernancePolicyStepsApiClient() {
    super(null);
  }

  @Override
  public BatchSolveFeaturesResponse batchSolveFeatures(BatchSolveFeaturesRequest request) {
    solvedFeatureVectors += request.getFeatureVectorsCount();
    var solutions = new ArrayList<SolutionResponse>();
    request.getFeatureVectorsList().forEach(f -> solutions.add(SolutionResponse
        .newBuilder()
        .setFeatureVectorSolution(FeatureVectorSolution.FEATURE_VECTOR_SOLUTION_UNSPECIFIED)
        .build()));
    return BatchSolveFeaturesResponse.newBuilder().addAllSolutions(solutions).build();
  }

  public int getSolvedFeatureVectors() {
    return solvedFeatureVectors;
  }
}
