package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;

public interface DatasetAlertsAdder {

  int addAlertsFromDataset(long fromDatasetId, long toAnalysisId, ChunkHandler chunkHandler);

  interface ChunkHandler {

    void handle(AnalysisAlertChunk chunk);
  }
}
