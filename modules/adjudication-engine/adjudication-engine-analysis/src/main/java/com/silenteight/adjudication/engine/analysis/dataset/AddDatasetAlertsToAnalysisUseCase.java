package com.silenteight.adjudication.engine.analysis.dataset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertsAddedGateway;
import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdder;
import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdder.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class AddDatasetAlertsToAnalysisUseCase {

  private final DatasetAlertsAdder datasetAlertsAdder;
  private final AnalysisAlertsAddedGateway gateway;

  @Timed(
      value = "ae.analysis.use_cases",
      extraTags = { "package", "analysis" },
      histogram = true,
      percentiles = { 0.5, 0.95, 0.99 }
  )
  int addDatasetAlerts(String analysis, String dataset) {
    long analysisId = ResourceName.create(analysis).getLong("analysis");
    long datasetId = ResourceName.create(dataset).getLong("datasets");

    log.debug("Adding alerts from dataset to analysis: dataset={}, analysis={}", dataset, analysis);

    var chunkHandler = new AnalysisAlertChunkHandler();
    var alertCount = datasetAlertsAdder.addAlertsFromDataset(datasetId, analysisId, chunkHandler);

    log.info(
        "Added alerts from dataset to analysis: dataset={}, analysis={}, alertCount={}", dataset,
        analysis, alertCount);

    return alertCount;
  }

  @RequiredArgsConstructor
  private class AnalysisAlertChunkHandler implements ChunkHandler {

    @Override
    public void handle(AnalysisAlertChunk chunk) {
      chunk.toAnalysisAlertsAdded().ifPresent(gateway::send);
    }
  }
}
