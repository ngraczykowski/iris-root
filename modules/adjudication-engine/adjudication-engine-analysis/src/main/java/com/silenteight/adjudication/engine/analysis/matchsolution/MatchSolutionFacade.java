package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchSolutionFacade {

  private final SolveAnalysisMatchesUseCase solveAnalysisMatchesUseCase;

  public void solveAnalysisMatches(long analysisId) {
    solveAnalysisMatchesUseCase.solveAnalysisMatches(analysisId);
  }
}
