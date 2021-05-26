package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.analysis.AnalysisEntity.Status;
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient.CannotGetRecommendationsException;
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class TimeoutHandler {
  
  private final AnalysisServiceClient analysisServiceClient;
  private final RecommendationServiceClient recommendationServiceClient;
  private final AnalysisRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  @Scheduled(fixedRate = 60 * 1000, initialDelay = 2000)
  @Transactional
  void process() {
    findInProgressTimeoutAnalyses().forEach(this::tryToHandleAnalysis);
  }

  private void tryToHandleAnalysis(AnalysisEntity entity) {
    try {
      handleAnalysis(entity);
    } catch (RuntimeException ex) {
      log.error("Error on handling analysis timeout", ex);
      entity.setStatus(Status.TIMEOUT_ERROR);
    }

    repository.save(entity);
  }

  private void handleAnalysis(AnalysisEntity entity) {
    if (hasPendingAlerts(entity.getName())) {
      handleTimeoutException(entity);
    } else {
      entity.setStatus(Status.COMPLETED);
    }
  }

  private void handleTimeoutException(AnalysisEntity entity)  {
    entity.setStatus(Status.TIMEOUT_ERROR);
    tryToCallForRecommendations(entity.getName(), entity.getDataset());
    eventPublisher.publishEvent(new AnalysisTimeoutEvent(entity.getId()));
  }

  private void tryToCallForRecommendations(String name, String dataset) {
    var request = GetRecommendationsDto.builder()
        .analysis(name)
        .dataset(dataset)
        .build();

    try {
      recommendationServiceClient.getRecommendations(request).forEach(this::publishRecommendation);
    } catch (CannotGetRecommendationsException ex) {
      log.error("Cannot get remaining recommendations", ex);
    }
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
