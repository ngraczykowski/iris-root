package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
class CreatePendingRecommendationsUseCase {

  private final PendingRecommendationDataAccess dataAccess;

  @Timed(value = "ae.analysis.use_cases", extraTags = {
      "package", "pendingrecommendation" }, histogram = true,
      percentiles = { 0.5, 0.95, 0.99 })
  @Transactional
  boolean createPendingRecommendations(long analysisId) {
    var pendingCount = dataAccess.createPendingRecommendations(analysisId);

    log.info("Created pending recommendations: analysis=analysis/{}, pendingCount={}",
        analysisId, pendingCount);

    return pendingCount > 0;
  }
}
