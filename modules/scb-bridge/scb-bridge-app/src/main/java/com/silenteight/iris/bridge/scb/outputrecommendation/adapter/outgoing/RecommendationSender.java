/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation.GnsRtRecommendationService;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationSender implements RecommendationPublisher {

  private final Optional<CbsRecommendationService> cbsRecommendationService;
  private final GnsRtRecommendationService gnsRtRecommendationService;
  private final ScbRecommendationService scbRecommendationService;

  @Override
  public void publishCompleted(RecommendationsGeneratedEvent event) {
    scbRecommendationService.saveRecommendations(event.recommendations());
    notifyServicesAboutRecommendations(event);
  }

  @Override
  public void publishError(ErrorRecommendationsGeneratedEvent event) {
    notifyServicesAboutErrorRecommendations(event);
  }

  private void notifyServicesAboutRecommendations(RecommendationsGeneratedEvent event) {
    log.info(
        "Notifying services about Recommendations generated for batchId: {}, batchMetadata: {}",
        event.batchId(), event.batchMetadata());

    var batchSource = event.batchMetadata().batchSource();
    switch (batchSource) {
      case CBS -> cbsRecommendationService.get().recommend(event.recommendations());
      case GNS_RT -> gnsRtRecommendationService.recommend(event.recommendations());
      default -> throw new IllegalStateException(
          "Unrecognized BatchSource type " + batchSource + " in batchMetadata");
    }
  }

  private void notifyServicesAboutErrorRecommendations(ErrorRecommendationsGeneratedEvent event) {
    log.info(
        "Notifying services about Error Recommendations for batchId: {}, event: {}",
        event.batchId(), event);

    var batchSource = event.batchMetadata().batchSource();
    switch (batchSource) {
      case CBS -> log.error("CBS path not yet implemented"); // TODO: implement
      case GNS_RT -> gnsRtRecommendationService.batchFailed(
          event.batchId(), event.errorDescription());
      default -> throw new IllegalStateException(
          "Unrecognized BatchSource type " + batchSource + " in batchMetadata");
    }
  }

}
