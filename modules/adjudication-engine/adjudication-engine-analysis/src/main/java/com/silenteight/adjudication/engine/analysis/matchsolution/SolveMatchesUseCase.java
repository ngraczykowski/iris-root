package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class SolveMatchesUseCase {

  private final UnsolvedMatchesReader unsolvedMatchesReader;
  private final GovernancePolicyStepsApiClient solvingApiClient;
  private final CreateMatchSolutionsUseCase createMatchSolutionsUseCase;

  void solveMatches(SolveMatchesRequest request) {
    var matchCount = 0;

    do {
      matchCount = unsolvedMatchesReader.readInChunks(
          request.getAnalysisId(), new SolvedMatchChunkHandler(request));
    } while (matchCount > 0);
  }

  @RequiredArgsConstructor
  private final class SolvedMatchChunkHandler implements ChunkHandler {

    private final SolveMatchesRequest solveRequest;

    @Override
    public void handle(UnsolvedMatchesChunk chunk) {
      var request = chunk.toBatchSolveFeaturesRequest(
          solveRequest.getPolicy(), solveRequest.getFeatureCollection());

      var response = solvingApiClient.batchSolveFeatures(request);

      var solutionCollection = chunk.toMatchSolutionCollection(
          solveRequest.getAnalysisId(), response.getSolutionsList());

      createMatchSolutionsUseCase.createMatchSolutions(solutionCollection);
    }
  }
}
