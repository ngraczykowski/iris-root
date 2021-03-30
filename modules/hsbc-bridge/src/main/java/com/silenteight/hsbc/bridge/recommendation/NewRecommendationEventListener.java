package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
class NewRecommendationEventListener {

  private final RecommendationRepository repository;

  @EventListener
  @Transactional
  void onNewRecommendationEvent(NewRecommendationEvent event){
    var recommendation = event.getRecommendation();

    repository.save(new RecommendationEntity(recommendation));

    log.info("Recommendation for an alert:{} has been stored", recommendation.getAlert());
  }
}
