package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
class HandleAddedAnalysisDatasetsUseCase {

  private final CreatePendingRecommendationsUseCase createPendingRecommendationsUseCase;

  private final ApplicationEventPublisher applicationEventPublisher;

  Optional<PendingRecommendations> handleAddedAnalysisAlerts(
      AddedAnalysisAlerts addedAnalysisDatasets) {

    var builder = PendingRecommendations.newBuilder();

    log.debug("Handling AnalysisAlert={}", addedAnalysisDatasets);

    var pendingCount = addedAnalysisDatasets
        .getAnalysisAlertsList()
        .stream()
        .mapToLong(name -> ResourceName.create(name).getLong("analysis"))
        .distinct()
        .filter(createPendingRecommendationsUseCase::createPendingRecommendations)
        .mapToObj(analysisId -> builder.addAnalysis("analysis/" + analysisId))
        .count();

    log.debug("Pending analysis={}", builder.build());
    applicationEventPublisher.publishEvent(builder.build());
    return pendingCount != 0 ? Optional.of(builder.build()) : Optional.empty();
  }
}
