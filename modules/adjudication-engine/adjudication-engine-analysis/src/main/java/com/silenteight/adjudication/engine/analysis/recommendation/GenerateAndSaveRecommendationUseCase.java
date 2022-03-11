package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
class GenerateAndSaveRecommendationUseCase {

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;



  Optional<RecommendationsGenerated> generateAndSaveRecommendations(String analysisName) {
    var recommendationInfos = generateRecommendationInfos(analysisName);

    if (recommendationInfos.isEmpty())
      return Optional.empty();

    return Optional.of(createRecommendationGenerated(analysisName, recommendationInfos));
  }

  private List<RecommendationInfo> generateRecommendationInfos(String analysisName) {
    return generateRecommendationsUseCase.generateRecommendations(
        analysisName);
  }

  private RecommendationsGenerated createRecommendationGenerated(
      String analysisName, List<RecommendationInfo> recommendationsInfo) {

    return RecommendationsGenerated
        .newBuilder()
        .addAllRecommendationInfos(recommendationsInfo)
        .setAnalysis(analysisName)
        .build();
  }
}
