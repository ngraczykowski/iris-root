package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
class HandleAddedAnalysisDatasetsUseCase {

  private final CreatePendingRecommendationsUseCase createPendingRecommendationsUseCase;

  @Transactional
  Optional<PendingRecommendations> handleAddedAnalysisDatasets(
      AddedAnalysisDatasets addedAnalysisDatasets) {

    var builder = PendingRecommendations.newBuilder();

    var pendingCount = addedAnalysisDatasets
        .getAnalysisDatasetsList()
        .stream()
        .mapToLong(name -> ResourceName.create(name).getLong("analysis"))
        .distinct()
        .filter(createPendingRecommendationsUseCase::createPendingRecommendations)
        .mapToObj(analysisId -> builder.addAnalysis("analysis/" + analysisId))
        .count();

    return pendingCount != 0 ? Optional.of(builder.build()) : Optional.empty();
  }
}
