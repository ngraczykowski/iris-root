package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.SolutionResponse;

import java.time.Duration;
import java.util.ArrayList;

public class MockGovernancePolicyStepsApiClient extends GovernancePolicyStepsApiClient {

  private int solvedFeatureVectors = 0;

  public MockGovernancePolicyStepsApiClient() {
    super(null, Duration.ZERO);
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
