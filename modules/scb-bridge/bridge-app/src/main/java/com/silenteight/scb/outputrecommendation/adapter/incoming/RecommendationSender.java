package com.silenteight.scb.outputrecommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.port.outgoing.RecommendationPublisher;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationSender implements RecommendationPublisher {

  @Override
  public void publishCompleted(RecommendationsGeneratedEvent event) {
    // TODO: implement
  }

  @Override
  public void publishError(ErrorRecommendationsGeneratedEvent event) {
    // TODO: implement
  }
}
