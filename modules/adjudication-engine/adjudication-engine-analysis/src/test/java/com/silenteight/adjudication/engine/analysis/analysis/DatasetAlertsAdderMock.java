package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.Setter;

class DatasetAlertsAdderMock implements DatasetAlertsAdder {

  private static final long CHUNK_SIZE = 10;

  @Setter
  private long alertCount = 15;

  @Override
  public int addAlertsFromDataset(
      long fromDatasetId, long toAnalysisId, ChunkHandler chunkHandler) {

    return 0;
  }
}
