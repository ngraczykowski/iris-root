package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationSender implements RecommendationPublisher {

  private final CbsRecommendationService cbsRecommendationService;
  private final ScbRecommendationService scbRecommendationService;

  @Override
  public void publishCompleted(RecommendationsGeneratedEvent event) {
    cbsRecommendationService.recommend(event.recommendations());
    scbRecommendationService.saveRecommendations(event.recommendations());
  }

  @Override
  public void publishError(ErrorRecommendationsGeneratedEvent event) {
    // TODO: implement
  }
}
