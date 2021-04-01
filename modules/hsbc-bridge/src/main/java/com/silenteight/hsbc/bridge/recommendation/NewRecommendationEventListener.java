package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
class NewRecommendationEventListener {

  private final ApplicationEventPublisher eventPublisher;
  private final RecommendationRepository repository;

  @EventListener
  @Transactional
  public void onNewRecommendationEvent(NewRecommendationEvent event) {
    var recommendation = event.getRecommendation();
    var alert = recommendation.getAlert();

    repository.save(new RecommendationEntity(recommendation));

    log.info("Recommendation for an alert:{} has been stored", alert);

    eventPublisher.publishEvent(new AlertRecommendationReadyEvent(alert));
  }
}
