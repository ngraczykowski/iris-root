package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation.GnsRtRecommendationService;
import com.silenteight.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationSender implements RecommendationPublisher {

  private final CbsRecommendationService cbsRecommendationService;
  private final GnsRtRecommendationService gnsRtRecommendationService;
  private final ScbRecommendationService scbRecommendationService;

  @Override
  public void publishCompleted(RecommendationsGeneratedEvent event) {
    notifyServicesAboutRecommendations(event);
    scbRecommendationService.saveRecommendations(event.recommendations());
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
      case CBS -> cbsRecommendationService.recommend(event.recommendations());
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
