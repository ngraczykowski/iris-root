package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationFacade {

  private final RegistrationService registrationService;
  private final RecommendationProcessor recommendationProcessor;
  private final RecommendationRepository recommendationRepository;
  private final BatchStatisticsService batchStatisticsService;

  public void proceedReadyRecommendations(ProceedReadyRecommendationsCommand command) {
    recommendationProcessor.proceedReadyRecommendations(command.recommendationsWithMetadata());
  }

  public void proceedBatchTimeout(ProceedBatchTimeoutCommand command) {
    recommendationProcessor.createTimedOutRecommendations(
        command.analysisName(), command.alertNames());
  }

  public RecommendationsResponse getRecommendationsResponse(GetRecommendationCommand command) {
    var batchWithAlerts = registrationService.getBatchWithAlerts(command.analysisName());
    var recommendations = recommendationRepository.findByAnalysisName(command.analysisName());
    var batchStatistics = batchStatisticsService.createBatchStatistics(
        batchWithAlerts.alerts(),
        recommendations);

    return RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts,
        recommendations,
        OffsetDateTime.now(),
        batchStatistics);
  }
}
