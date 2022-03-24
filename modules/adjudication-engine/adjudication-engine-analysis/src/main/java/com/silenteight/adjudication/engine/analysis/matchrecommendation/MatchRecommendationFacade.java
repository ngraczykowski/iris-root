package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchRecommendationFacade {

  private final HandleSolvedMatchesUseCase handleSolvedMatchesUseCase;

  public Optional<MatchRecommendationsGenerated> generateMatchRecommendation(
      MatchesSolved matchesSolved) {
    return handleSolvedMatchesUseCase.handleMatchesSolved(matchesSolved);
  }
}
