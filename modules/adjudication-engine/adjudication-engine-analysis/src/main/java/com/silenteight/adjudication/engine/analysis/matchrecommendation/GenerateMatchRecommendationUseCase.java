package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import com.silenteight.adjudication.api.v3.MatchRecommendation;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class GenerateMatchRecommendationUseCase {

  public Optional<MatchRecommendation> generateMatchRecommendation(MatchesSolved matchesSolved) {
    return Optional.empty();
  }
}
