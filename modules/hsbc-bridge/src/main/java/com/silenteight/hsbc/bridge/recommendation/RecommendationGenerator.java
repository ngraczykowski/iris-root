package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.event.AlertRecommendationReadyEvent;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * FOR DEV USAGE ONLY
 */
@RequiredArgsConstructor
public class RecommendationGenerator {

  private final RecommendationRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void generate(@NonNull GenerationRequest request) {
    var recommendations = request.getAlerts().stream()
        .map(RecommendationGenerator::createDummyRecommendation);

    handleDummyRecommendations(request.getAnalysis(), recommendations);
  }

  private void handleDummyRecommendations(String analysis, Stream<RecommendationDto> recommendations) {
    recommendations.forEach(r -> {
      repository.save(new RecommendationEntity(r));
      eventPublisher.publishEvent(new AlertRecommendationReadyEvent(r.getAlert()));
    });

    eventPublisher.publishEvent(new AnalysisCompletedEvent(analysis));
  }

  private static RecommendationDto createDummyRecommendation(String alert) {
    return RecommendationDto.builder()
        .alert(alert)
        .name("recommendations/recommendation-" + UUID.randomUUID())
        .date(OffsetDateTime.now())
        .recommendedAction("MANUAL_INVESTIGATION")
        .recommendationComment("S8 Recommendation: Manual Investigation")
        .build();
  }

  public interface GenerationRequest {

    String getAnalysis();
    Collection<String> getAlerts();
  }

}
