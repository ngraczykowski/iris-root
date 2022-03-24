package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

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
    var recommendations = getRecommendations(command);
    var batchStatistics = batchStatisticsService.createBatchStatistics(
        batchWithAlerts.alerts(),
        recommendations);

    return RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts,
        recommendations,
        OffsetDateTime.now(),
        batchStatistics);
  }

  private List<RecommendationWithMetadata> getRecommendations(GetRecommendationCommand command) {
    if (CollectionUtils.isEmpty(command.alertNames())) {
      return recommendationRepository.findByAnalysisName(command.analysisName());
    }
    return recommendationRepository.findByAnalysisNameAndAlertNameIn(
        command.analysisName(), command.alertNames());
  }
}
