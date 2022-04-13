package com.silenteight.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.scb.outputrecommendation.domain.model.BatchMetadata;
import com.silenteight.scb.outputrecommendation.domain.model.BatchSource;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.infrastructure.QcoRecommendationProperties;
import com.silenteight.scb.qco.QcoFacade;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class QcoRecommendationService {

  private final QcoRecommendationProperties qcoProperties;
  private final QcoFacade qcoFacade;

  RecommendationsGeneratedEvent process(RecommendationsGeneratedEvent recommendationsEvent) {
    if (!isQcoAllowed(recommendationsEvent)) {
      return recommendationsEvent;
    }

    return Try.of(() -> updateWithQco(recommendationsEvent))
        .onFailure(e -> log.error(
            "Failed to update recommendations with QCO, batchId: {}",
            recommendationsEvent.batchId(), e))
        .getOrElse(recommendationsEvent);
  }

  private RecommendationsGeneratedEvent updateWithQco(
      RecommendationsGeneratedEvent recommendationsEvent) {
    var updatedRecommendations = updateRecommendations(recommendationsEvent.recommendations());

    return recommendationsEvent.toBuilder()
        .recommendations(updatedRecommendations)
        .build();
  }

  private List<Recommendation> updateRecommendations(List<Recommendation> recommendations) {
    return recommendations.stream()
        .map(this::updateRecommendation)
        .toList();
  }

  private Recommendation updateRecommendation(Recommendation recommendation) {
    var qcoRecommendationAlert = QcoRecommendationAlertMapper.map(recommendation);
    var responseQcoRecommendation = qcoFacade.process(qcoRecommendationAlert);
    return overrideMatches(recommendation, responseQcoRecommendation);
  }

  private Recommendation overrideMatches(
      Recommendation recommendation, QcoRecommendationAlert qcoRecommendationAlert) {
    return recommendation.toBuilder()
        .matches(MatchOverrider.override(recommendation.matches(), qcoRecommendationAlert))
        .build();
  }

  private boolean isQcoAllowed(RecommendationsGeneratedEvent event) {
    return qcoProperties.enabled() && isCbsBatch(event.batchMetadata());
  }

  private boolean isCbsBatch(BatchMetadata batchMetadata) {
    return batchMetadata.batchSource() == BatchSource.CBS;
  }
}
