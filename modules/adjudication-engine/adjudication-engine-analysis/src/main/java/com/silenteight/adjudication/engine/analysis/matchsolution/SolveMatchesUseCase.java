package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.matchsolution.UnsolvedMatchesReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SolveMatchesRequest;
import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
class SolveMatchesUseCase {

  private final UnsolvedMatchesReader unsolvedMatchesReader;
  private final PolicyStepsClient solvingApiClient;
  private final CreateMatchSolutionsUseCase createMatchSolutionsUseCase;

  List<String> solveMatches(SolveMatchesRequest request) {
    var chunkHandler = new SolvedMatchChunkHandler(request);

    if (log.isDebugEnabled()) {
      log.debug("Starting solving matches: analysisId={}, policy={}, featureCount={}",
          request.getAnalysisId(), request.getPolicy(), request.getFeatureCount());
    }

    var matchCount = 0;
    var totalCount = 0;
    do {
      matchCount = unsolvedMatchesReader.readInChunks(
          request.getAnalysisId(), chunkHandler);
      totalCount += matchCount;
    } while (matchCount > 0);

    if (totalCount > 0) {
      log.info("Finished solving matches: analysisId={}, matchCount={}",
          request.getAnalysisId(), totalCount);
    } else {
      log.debug("No matches solved: analysisId={}", request.getAnalysisId());
    }

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

      if (log.isDebugEnabled()) {
        log.debug(
            "Requesting solutions for chunk of unsolved matches: matches={}",
            chunk.getMatchNames());
      }

      var response = solvingApiClient.batchSolveFeatures(request);

      var solutionCollection = chunk.toMatchSolutionCollection(
          solveRequest.getAnalysisId(), response.getSolutionsList());

      createMatchSolutionsUseCase.createMatchSolutions(solutionCollection);

      solvedMatches.addAll(chunk.getMatchNames());
    }
  }
}
