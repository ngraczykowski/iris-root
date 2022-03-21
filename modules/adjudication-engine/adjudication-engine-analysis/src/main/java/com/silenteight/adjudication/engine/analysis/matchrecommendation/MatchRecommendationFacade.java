package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v3.MatchRecommendation;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchRecommendationFacade {

  private final GenerateMatchRecommendationUseCase generateMatchRecommendationUseCase;

  public Optional<MatchRecommendation> generateMatchRecommendation(MatchesSolved matchesSolved) {
    return generateMatchRecommendationUseCase.generateMatchRecommendation(matchesSolved);
  }
}
