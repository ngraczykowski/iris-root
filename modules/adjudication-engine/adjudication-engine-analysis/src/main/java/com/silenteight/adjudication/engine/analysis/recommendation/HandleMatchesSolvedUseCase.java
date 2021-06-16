package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class HandleMatchesSolvedUseCase {

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;

  Optional<RecommendationsGenerated> handleMatchesSolved(MatchesSolved matchesSolved) {
    return generateRecommendationsUseCase.generateRecommendations(matchesSolved.getAnalysis());
  }
}
