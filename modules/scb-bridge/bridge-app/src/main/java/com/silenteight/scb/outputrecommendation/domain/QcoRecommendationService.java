package com.silenteight.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.BatchMetadata;
import com.silenteight.scb.outputrecommendation.domain.model.BatchSource;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.infrastructure.QcoRecommendationProperties;
import com.silenteight.scb.outputrecommendation.infrastructure.QcoRecommendationProvider;

import io.vavr.control.Try;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class QcoRecommendationService {

  private final QcoRecommendationProperties qcoProperties;
  private final QcoRecommendationProvider qcoRecommendationProvider;

  RecommendationsGeneratedEvent process(RecommendationsGeneratedEvent recommendationsEvent) {
    if (!isQcoAllowed()) {
      return recommendationsEvent;
    }

    return Try.of(() -> updateMatches(recommendationsEvent))
        .onSuccess(recommendation ->
            log.debug("Recommendation was processed by QCO module {}", recommendation.toString()))
        .onFailure(e -> log.error(
            "Failed to update recommendations with QCO, batchId: {}",
            recommendationsEvent.batchId(), e))
        .getOrElse(recommendationsEvent);
  }

  private RecommendationsGeneratedEvent updateMatches(RecommendationsGeneratedEvent event) {
    boolean onlyMark = isOnlyMark(event.batchMetadata());
    return event.toBuilder()
        .recommendations(event.recommendations().stream()
            .map(recommendation -> updateRecommendation(recommendation, onlyMark))
            .toList())
        .build();
  }

  private Recommendation updateRecommendation(Recommendation recommendation, boolean onlyMark) {
    var qcoRecommendationAlert = QcoRecommendationAlertMapper.map(recommendation, onlyMark);
    var responseQcoRecommendation = qcoRecommendationProvider.process(qcoRecommendationAlert);
    return MatchOverrider.overrideMatches(recommendation, responseQcoRecommendation);
  }

  private boolean isQcoAllowed() {
    return qcoProperties.enabled();
  }

  private boolean isOnlyMark(BatchMetadata batchMetadata) {
    return batchMetadata.batchSource() == BatchSource.GNS_RT;
  }
}
