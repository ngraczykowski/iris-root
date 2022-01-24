package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.GetRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.exception.CannotGetRecommendationsException;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStatistics;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationFacade {

  private final RecommendationService recommendationService;
  private final RecommendationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;
  private final RecommendationsStatisticsService recommendationsStatisticsService;

  public void proceedReadyRecommendations(String analysisName) {
    var alertNames = getAndStoreRecommendations(analysisName).stream()
        .map(RecommendationWithMetadata::alertName)
        .toList();

    eventPublisher.publish(new RecommendationsReceivedEvent(analysisName, alertNames));
  }

  public List<RecommendationWithMetadata> getRecommendations(GetRecommendationsCommand command) {
    return recommendationRepository.findByAnalysisName(command.analysisName());
  }

  public RecommendationsStatistics getRecommendationsStatistics(String analysisName) {
    return recommendationsStatisticsService.createRecommendationsStatistics(analysisName);
  }

  private List<RecommendationWithMetadata> getAndStoreRecommendations(String analysisName) {
    return Try.of(() -> recommendationService.getRecommendations(analysisName))
        .onSuccess(recommendationRepository::saveAll)
        .onFailure(e -> log.error("Cannot get recommendations for analysis={}", analysisName, e))
        .getOrElseThrow(CannotGetRecommendationsException::new);
  }
}
