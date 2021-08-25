package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.PendingRecommendationFacade;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("FeatureEnvy")
@RequiredArgsConstructor
@Service
@Slf4j
class AddDatasetsToAnalysisUseCase {

  @NonNull
  private final AnalysisDatasetRepository repository;

  @NonNull
  private final DatasetAlertsReader datasetAlertsReader;

  @NonNull
  private final PendingRecommendationFacade pendingRecommendationFacade;

  @Transactional
  List<AnalysisDatasetEntity> addDatasets(String analysis, List<String> datasets) {
    long analysisId = ResourceName.create(analysis).getLong("analysis");

    log.debug("Adding datasets={} to analysis={}", datasets, analysis);

    return datasets.stream()
        .map(dataset -> {
          // FIXME(ahaczewski): Make this opration idempotent, as it might fail when publishing
          //  message to RabbitMQ, but after commit has succeeded.
          var datasetId = ResourceName.create(dataset).getLong("datasets");
          datasetAlertsReader.read(
              analysisId, datasetId, new AnalysisAlertChunkHandler(pendingRecommendationFacade));
          var entity = new AnalysisDatasetEntity(analysisId, datasetId);
          return repository.save(entity);
        })
        .collect(toList());
  }

  @RequiredArgsConstructor
  private static class AnalysisAlertChunkHandler implements ChunkHandler {

    @NonNull
    private final PendingRecommendationFacade pendingRecommendationFacade;

    @Override
    public void handle(AnalysisAlertChunk chunk) {
      var eventBuilder = AddedAnalysisAlerts.newBuilder();
      chunk.forEach(aa -> eventBuilder.addAnalysisAlerts(aa.toName()));
      log.debug("Sending chun={} to pending recommendation", chunk);
      pendingRecommendationFacade.handleAddedAnalysisDatasets(eventBuilder.build());
    }
  }
}
