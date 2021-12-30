package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.exceptions.CannotGetRecommendationsException;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationFacade {

  private final RecommendationService recommendationService;
  private final RecommendationEventPublisher eventPublisher;
  private final RecommendationRepository recommendationRepository;

  public void proceedReadyRecommendations(String analysisName) {
    var alertNames = getAndStoreRecommendations(analysisName)
        .stream()
        .map(RecommendationWithMetadata::alertName)
        .collect(Collectors.toList());

    eventPublisher.publish(new RecommendationsReceivedEvent(analysisName, alertNames));
  }

  private List<RecommendationWithMetadata> getAndStoreRecommendations(String analysisName) {
    return Try.of(() -> recommendationService.getRecommendations(analysisName))
        .onSuccess(recommendationRepository::saveAll)
        .onFailure(e -> log.error("Cannot get recommendations for analysis={}", analysisName, e))
        .getOrElseThrow(CannotGetRecommendationsException::new);
  }
}
