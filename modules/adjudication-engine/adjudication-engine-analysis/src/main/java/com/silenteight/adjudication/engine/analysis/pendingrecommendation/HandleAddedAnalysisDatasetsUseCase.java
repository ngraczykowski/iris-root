package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
class HandleAddedAnalysisDatasetsUseCase {

  private final CreatePendingRecommendationsUseCase createPendingRecommendationsUseCase;

  Optional<PendingRecommendations> handleAddedAnalysisDatasets(
      AddedAnalysisDatasets addedAnalysisDatasets) {

    var builder = PendingRecommendations.newBuilder();

    addedAnalysisDatasets
        .getAnalysisDatasetsList()
        .stream()
        .mapToLong(name -> ResourceName.create(name).getLong("analysis"))
        .distinct()
        .forEach(analysisId -> {
          var pending =
              createPendingRecommendationsUseCase.createPendingRecommendations(analysisId);

          if (pending) {
            builder.addAnalysis("analysis/" + analysisId);
          }
        });

    if (builder.getAnalysisCount() == 0) {
      return Optional.empty();
    }

    return Optional.of(builder.build());
  }
}
