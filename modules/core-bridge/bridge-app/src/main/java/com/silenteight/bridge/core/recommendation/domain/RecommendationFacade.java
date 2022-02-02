package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationCommand;
import com.silenteight.bridge.core.recommendation.domain.exception.CannotGetRecommendationsException;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RegistrationService;
import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationFacade {

  private final RegistrationService registrationService;
  private final RecommendationService recommendationService;
  private final RecommendationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;

  public void proceedReadyRecommendations(String analysisName) {
    var alertNames = getAndStoreRecommendations(analysisName).stream()
        .map(RecommendationWithMetadata::alertName)
        .toList();

    eventPublisher.publish(new RecommendationsReceivedEvent(analysisName, alertNames));
  }

  public RecommendationsResponse getRecommendationsResponse(GetRecommendationCommand command) {
    var batchWithAlerts = registrationService.getBatchWithAlerts(command.analysisName());
    var recommendations = recommendationRepository.findByAnalysisName(command.analysisName());
    return RecommendationMapper.toRecommendationsResponse(
        batchWithAlerts,
        recommendations,
        OffsetDateTime.now());
  }

  private List<RecommendationWithMetadata> getAndStoreRecommendations(String analysisName) {
    return Try.of(() -> recommendationService.getRecommendations(analysisName))
        .onSuccess(recommendationRepository::saveAll)
        .onFailure(e -> log.error("Cannot get recommendations for analysis={}", analysisName, e))
        .getOrElseThrow(CannotGetRecommendationsException::new);
  }
}
