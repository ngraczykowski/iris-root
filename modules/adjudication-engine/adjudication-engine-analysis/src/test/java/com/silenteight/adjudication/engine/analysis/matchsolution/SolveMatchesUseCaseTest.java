package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFixture.createSolutionResponses;
import static com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFixture.createSolveMatchesRequest;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolveMatchesUseCaseTest {

  @Mock
  private GovernancePolicyStepsApiClient governancePolicyStepsApiClient;
  @Mock
  private CreateMatchSolutionsUseCase createMatchSolutionsUseCase;
  private SolveMatchesUseCase solveMatchesUseCase;

  @BeforeEach
  void setUp() {
    var unsolvedMatchesReader = new InMemoryUnsolvedMatchesReader();
    solveMatchesUseCase = new SolveMatchesUseCase(
        unsolvedMatchesReader, governancePolicyStepsApiClient, createMatchSolutionsUseCase);
  }

  @Test
  void shouldSolveAllMatches() {
    when(governancePolicyStepsApiClient.batchSolveFeatures(any()))
        .thenReturn(BatchSolveFeaturesResponse
            .newBuilder()
            .addAllSolutions(createSolutionResponses(10))
            .build());

    var matchesRequest = createSolveMatchesRequest(10);
    solveMatchesUseCase.solveMatches(matchesRequest);

    verify(governancePolicyStepsApiClient, times(10)).batchSolveFeatures(any());
  }
}
