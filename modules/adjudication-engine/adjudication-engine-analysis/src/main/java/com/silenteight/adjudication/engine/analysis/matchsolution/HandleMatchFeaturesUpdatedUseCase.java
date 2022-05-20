package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;
import com.silenteight.adjudication.internal.v1.MatchFeaturesUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Slf4j
@Service
class HandleMatchFeaturesUpdatedUseCase {

  private final AnalysisFacade analysisFacade;
  private final SolveAnalysisMatchesUseCase solveAnalysisMatchesUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<MatchesSolved> handleMatchFeaturesUpdated(MatchFeaturesUpdated matchFeaturesUpdated) {
    var matches = matchFeaturesUpdated.getMatchesList();
    var analysis =
        analysisFacade.findAnalysisByPendingMatches(matches);

    if (log.isDebugEnabled()) {
      log.debug("Trying to solve matches: analysis={}, matchCount={}", analysis, matches.size());
    }

    var solver = new MatchSolver(matches.size());
    var matchesSolvedList = solver.solveAll(analysis);

    log.info("Finished solving updated matches: analysis={}, updatedMatchCount={}"
            + ", totalSolvedCount={}",
        analysis, matches.size(), solver.totalSolvedCount);

    return matchesSolvedList;
  }

  @RequiredArgsConstructor
  private final class MatchSolver {

    private final int updatedMatchCount;
    private int totalSolvedCount = 0;

    List<MatchesSolved> solveAll(List<String> analysisNames) {
      return analysisNames.stream()
          .map(this::solveAnalysisMatches)
          .filter(Objects::nonNull)
          .collect(toUnmodifiableList());
    }

    @Nullable
    @SuppressWarnings("FeatureEnvy")
    private MatchesSolved solveAnalysisMatches(String analysisName) {
      var solvedMatches = solveAnalysisMatchesUseCase.solveAnalysisMatches(analysisName);

      if (solvedMatches.isEmpty()) {
        log.debug("No match solved from the updated matches: analysis={}, updatedMatchCount={}",
            analysisName, updatedMatchCount);
        return null;
      }

      totalSolvedCount += solvedMatches.size();

      log.info(
          "Solved matches for analysis: analysis={}, updatedMatchCount={}, solvedMatchCount={}",
          analysisName, updatedMatchCount, solvedMatches.size());

      return MatchesSolved.newBuilder()
          .setAnalysis(analysisName)
          .addAllMatches(solvedMatches)
          .build();
    }
  }
}
