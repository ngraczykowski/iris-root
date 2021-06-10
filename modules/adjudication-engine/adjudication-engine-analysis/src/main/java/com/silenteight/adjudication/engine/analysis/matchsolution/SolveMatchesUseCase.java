package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
class SolveMatchesUseCase {

  private final UnsolvedMatchesReader unsolvedMatchesReader;
  private final GovernancePolicyStepsApiClient solvingApiClient;
  private final CreateMatchSolutionsUseCase createMatchSolutionsUseCase;

  List<String> solveMatches(SolveMatchesRequest request) {
    var chunkHandler = new SolvedMatchChunkHandler(request);

    var matchCount = 0;
    do {
      matchCount = unsolvedMatchesReader.readInChunks(
          request.getAnalysisId(), chunkHandler);
    } while (matchCount > 0);

    return chunkHandler.solvedMatches;
  }

  @RequiredArgsConstructor
  private final class SolvedMatchChunkHandler implements ChunkHandler {

    private final SolveMatchesRequest solveRequest;
    private final List<String> solvedMatches = new ArrayList<>();

    @Override
    public void handle(UnsolvedMatchesChunk chunk) {
      var request = chunk.toBatchSolveFeaturesRequest(
          solveRequest.getPolicy(), solveRequest.getFeatureCollection());

      var response = solvingApiClient.batchSolveFeatures(request);

      var solutionCollection = chunk.toMatchSolutionCollection(
          solveRequest.getAnalysisId(), response.getSolutionsList());

      createMatchSolutionsUseCase.createMatchSolutions(solutionCollection);

      solvedMatches.addAll(chunk.getMatchNames());
    }
  }
}
