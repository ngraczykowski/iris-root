package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
class HandleAddedAnalysisDatasetsUseCase {

  private final CreatePendingRecommendationsUseCase createPendingRecommendationsUseCase;

  // TODO(ahaczewski): Implement creating pending recommendations.
  Optional<PendingRecommendations> handleAddedAnalysisDatasets(
      AddedAnalysisDatasets addedAnalysisDatasets) {

    var analysisIds = addedAnalysisDatasets
        .getAnalysisDatasetsList()
        .stream()
        .map(name -> ResourceName.create(name).getLong("analysis"))
        .distinct()
        .collect(toList());

    return Optional.empty();
  }
}
