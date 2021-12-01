package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.recommendation.port.DeleteRecommendationUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
class DeleteRecommendationService implements DeleteRecommendationUseCase {

  private final RecommendationRepository recommendationRepository;

  @Override
  @Transactional
  public void delete(List<UUID> alertMessageIds) {

    log.info("Deleting recommendations: alertsCount={}, alerts={}",
        alertMessageIds.size(), alertMessageIds);

    int recommendationDeletedCount = recommendationRepository.deleteAllByAlertIdIn(alertMessageIds);

    log.info("Recommendations removed, count={}", recommendationDeletedCount);

  }
}
