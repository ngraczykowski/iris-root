package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class GenerateAndSaveRecommendationUseCase {

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;
  private final CreateRecommendationsUseCase createRecommendationsUseCase;

  RecommendationsGenerated generateAndSaveRecommendations(String analysisName) {
    var recommendationInfos = generateRecommendationInfos(analysisName);
    return createRecommendationGenerated(analysisName, recommendationInfos);
  }

  private List<RecommendationInfo> generateRecommendationInfos(String analysisName) {
    return generateRecommendationsUseCase.generateRecommendations(
        analysisName,
        this::createRecommendation);
  }

  @NotNull
  private RecommendationsGenerated createRecommendationGenerated(
      String analysisName, List<RecommendationInfo> recommendationsInfo) {
    return RecommendationsGenerated
        .newBuilder()
        .addAllRecommendationInfos(recommendationsInfo)
        .setAnalysis(analysisName)
        .build();
  }


  private List<RecommendationInfo> createRecommendation(SaveRecommendationRequest request) {
    return createRecommendationsUseCase.createRecommendations(
        request.getAnalysisId(), request.getAlertSolutions());
  }
}
