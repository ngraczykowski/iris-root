package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchRecommendationFacade {

  private final HandleSolvedMatchesUseCase handleSolvedMatchesUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Optional<MatchRecommendationsGenerated> generateMatchRecommendation(
      MatchesSolved matchesSolved) {
    return handleSolvedMatchesUseCase.handleMatchesSolved(matchesSolved);
  }
}
