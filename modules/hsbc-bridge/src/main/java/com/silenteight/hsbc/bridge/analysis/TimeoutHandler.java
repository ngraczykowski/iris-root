package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
class TimeoutHandler {
  
  private final AnalysisServiceClient analysisServiceClient;
  private final RecommendationServiceClient recommendationServiceClient;
  private final AnalysisRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Scheduled(fixedRate = 60 * 1000, initialDelay = 2000)
  @Transactional
  void process() {
    findInProgressTimeoutAnalyses().forEach(this::handleAnalysis);
  }

  private void handleAnalysis(AnalysisEntity entity) {
    if (hasPendingAlerts(entity.getName())) {
      handleTimeoutException(entity);
    } else {
      entity.setStatus(Status.COMPLETED);
    }

    repository.save(entity);
  }

  private void handleTimeoutException(AnalysisEntity entity) {
    entity.setStatus(Status.TIMEOUT_ERROR);
    callForRecommendations(entity.getName());
    eventPublisher.publishEvent(new AnalysisTimeoutEvent(entity.getId()));
  }

  private void callForRecommendations(String name) {
    var request = GetRecommendationsDto.builder()
        .analysis(name)
        .build();

    recommendationServiceClient.getRecommendations(request).forEach(this::publishRecommendation);
  }

  private List<AnalysisEntity> findInProgressTimeoutAnalyses() {
    return repository.findByTimeoutAtBeforeAndStatus(OffsetDateTime.now(), Status.IN_PROGRESS);
  }

  private boolean hasPendingAlerts(String analysis) {
    return analysisServiceClient.getAnalysis(analysis).hasPendingAlerts();
  }

  private void publishRecommendation(RecommendationDto recommendation) {
    eventPublisher.publishEvent(new NewRecommendationEvent(recommendation));
  }
}
