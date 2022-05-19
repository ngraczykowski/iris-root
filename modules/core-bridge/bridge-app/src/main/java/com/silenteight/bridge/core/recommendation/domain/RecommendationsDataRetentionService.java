package com.silenteight.bridge.core.recommendation.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class RecommendationsDataRetentionService {

  private final RecommendationRepository recommendationRepository;

  void performDataRetention(ProceedDataRetentionOnRecommendationsCommand command) {
    log.info("Clearing PII from [{}] recommendations", command.alertNames().size());
    if (CollectionUtils.isNotEmpty(command.alertNames())) {
      recommendationRepository.clearCommentAndPayload(command.alertNames());
    }
  }
}
