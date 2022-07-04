/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchMetadata;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.BatchSource;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.QcoRecommendationProperties;
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.QcoRecommendationProvider;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class QcoRecommendationService {

  private final QcoRecommendationProperties qcoProperties;
  private final QcoRecommendationProvider qcoRecommendationProvider;

  RecommendationsGeneratedEvent process(RecommendationsGeneratedEvent recommendationsEvent) {
    if (!isQcoAllowed()) {
      log.debug("The Qco processing is disabled");
      return recommendationsEvent;
    }
    return safeUpdateMatches(recommendationsEvent);
  }

  private RecommendationsGeneratedEvent safeUpdateMatches(
      RecommendationsGeneratedEvent recommendationsEvent) {
    try {
      var recommendation = updateMatches(recommendationsEvent);
      if (log.isTraceEnabled()) {
        log.trace("Recommendation was processed by QCO module {}", recommendation);
      }
      return recommendation;
    } catch (Exception e) {
      log.error(
          "Failed to update recommendations with QCO, batchId: {}",
          recommendationsEvent.batchId(),
          e);
      return recommendationsEvent;
    }
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
