package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.EVENT_EXCHANGE_NAME;
import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.RECOMMENDATIONS_GENERATED_ROUTING_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
class GenerateAndSaveRecommendationUseCase {

  private final GenerateRecommendationsUseCase generateRecommendationsUseCase;
  private final RabbitTemplate rabbitTemplate;


  Optional<RecommendationsGenerated> generateAndSaveRecommendations(String analysisName) {
    var recommendationInfos = generateRecommendationInfos(analysisName);

    if (recommendationInfos.isEmpty())
      return Optional.empty();

    var recommendation = createRecommendationGenerated(analysisName, recommendationInfos);

    log.info(
        "Sending recommendation for analysis:{}", recommendation.getAnalysis());

    rabbitTemplate.convertAndSend(
        EVENT_EXCHANGE_NAME, RECOMMENDATIONS_GENERATED_ROUTING_KEY, recommendation);

    return Optional.of(recommendation);
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
