package com.silenteight.adjudication.engine.analysis.agentexchange;

import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

public interface MissingMatchFeatureReader {

  int read(long analysisId, ChunkHandler chunkHandler);

  interface ChunkHandler {

    void handle(MissingMatchFeatureChunk chunk);

    default void finished() {
    }
  }
}
