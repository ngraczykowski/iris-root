package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class HandleMatchesSolvedUseCase {

  private final GenerateAndSaveRecommendationUseCase generateAndSaveRecommendationUseCase;

  Optional<RecommendationsGenerated> handleMatchesSolved(MatchesSolved matchesSolved) {
    if (log.isDebugEnabled()) {
      log.debug("Handling matches solved: analysis={}, matchCount={}",
          matchesSolved.getAnalysis(), matchesSolved.getMatchesCount());
    }

    return Optional.of(generateAndSaveRecommendationUseCase.generateAndSaveRecommendations(
        matchesSolved.getAnalysis()));
  }
}
