package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated;
import com.silenteight.adjudication.api.v3.MatchRecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.internal.v1.MatchesSolved;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
class HandleSolvedMatchesUseCase {

  private final GenerateMatchRecommendationUseCase generateMatchRecommendationUseCase;

  public Optional<MatchRecommendationsGenerated> handleMatchesSolved(MatchesSolved matchesSolved) {
    var analysisName = matchesSolved.getAnalysis();
    log.debug("Starting generating match recommendations: analysis={}", analysisName);

    var recommendationInfos = generateMatchRecommendationUseCase.solveMatches(analysisName);

    log.info("Finished generating match recommendations: analysis={}, matchRecommendationCount={}",
        analysisName, recommendationInfos.size());

    return Optional.of(createRecommendationGenerated(analysisName, recommendationInfos));
  }

  private static MatchRecommendationsGenerated createRecommendationGenerated(
      String analysisName, List<RecommendationInfo> recommendationsInfo) {

    return MatchRecommendationsGenerated
        .newBuilder()
        .addAllRecommendationInfos(recommendationsInfo)
        .setAnalysis(analysisName)
        .build();
  }
}
