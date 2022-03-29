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

    // TODO: code which checks if response should go to CBS or RT
    cbsRecommendationService.recommend(event.recommendations());
    gnsRtRecommendationService.recommend(event.recommendations());

    // TODO: should below be called for RT or CBS only
    scbRecommendationService.saveRecommendations(event.recommendations());
  }

  @Override
  public void publishError(ErrorRecommendationsGeneratedEvent event) {
    // TODO: code which checks if response should go to CBS or RT
    gnsRtRecommendationService.batchFailed(event.batchId(), event.errorDescription());
  }
}
