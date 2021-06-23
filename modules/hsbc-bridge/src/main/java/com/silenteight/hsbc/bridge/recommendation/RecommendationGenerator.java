package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.recommendation.event.AlertRecommendationInfo;
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FOR DEV USAGE ONLY
 */
@RequiredArgsConstructor
public class RecommendationGenerator {

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void generate(@NonNull GenerationRequest request) {
    var alerts = request.getAlerts();
    var analysis = request.getAnalysis();

    eventPublisher.publishEvent(RecommendationsGeneratedEvent.builder()
        .analysis(analysis)
        .alertRecommendationInfos(createAlertRecommendationInfos(alerts))
        .build());
  }

  private List<AlertRecommendationInfo> createAlertRecommendationInfos(Collection<String> alerts) {
    return alerts.stream()
        .map(a -> new AlertRecommendationInfo(a, "recommendation/1"))
        .collect(Collectors.toList());
  }

  public interface GenerationRequest {

    String getAnalysis();

    Collection<String> getAlerts();
  }

}
