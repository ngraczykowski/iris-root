package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;

public interface DatasetAlertsReader {

  int read(long analysisId, long datasetId, ChunkHandler chunkHandler);

  interface ChunkHandler {

    void handle(AnalysisAlertChunk chunk);

    default void finished() {
    }
  }
}
