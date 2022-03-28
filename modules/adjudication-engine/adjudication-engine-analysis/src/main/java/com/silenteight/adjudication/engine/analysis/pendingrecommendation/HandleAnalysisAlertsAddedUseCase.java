package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
class HandleAnalysisAlertsAddedUseCase {

  private final CreatePendingRecommendationsUseCase createPendingRecommendationsUseCase;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  Optional<PendingRecommendations> handleAnalysisAlertsAdded(
      AnalysisAlertsAdded analysisAlertsAdded) {

    var builder = PendingRecommendations.newBuilder();

    var pendingCount = analysisAlertsAdded
        .getAnalysisAlertsList()
        .stream()
        .mapToLong(name -> ResourceName.create(name).getLong("analysis"))
        .distinct()
        .filter(createPendingRecommendationsUseCase::createPendingRecommendations)
        .mapToObj(analysisId -> builder.addAnalysis("analysis/" + analysisId))
        .count();

    return pendingCount != 0 ? Optional.of(builder.build()) : Optional.empty();
  }
}
