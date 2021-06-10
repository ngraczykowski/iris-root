package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.MatchCategoriesUpdated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Service
class HandleMatchCategoriesUpdatedUseCase {

  private final SolveAnalysisMatchesUseCase solveAnalysisMatchesUseCase;

  Optional<MatchesSolved> handleMatchCategoriesUpdated(
      MatchCategoriesUpdated matchCategoriesUpdated) {

    var solvedMatches =
        solveAnalysisMatchesUseCase.solveAnalysisMatches(matchCategoriesUpdated.getAnalysis());

    if (solvedMatches.isEmpty()) {
      return empty();
    }

    return of(MatchesSolved.newBuilder()
        .setAnalysis(matchCategoriesUpdated.getAnalysis())
        .addAllMatches(solvedMatches)
        .build());
  }
}
