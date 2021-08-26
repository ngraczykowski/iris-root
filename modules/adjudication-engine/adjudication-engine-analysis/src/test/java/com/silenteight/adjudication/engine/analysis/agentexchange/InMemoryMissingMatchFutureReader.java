package com.silenteight.adjudication.engine.analysis.agentexchange;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createMissingMatchFeatureChunk;

class InMemoryMissingMatchFutureReader implements MissingMatchFeatureReader {

  private int chunks = 10;

  @Override
  public int read(long analysisId, ChunkHandler chunkHandler) {
    chunkHandler.handle(createMissingMatchFeatureChunk(10));
    if (chunks == 1)
      chunkHandler.finished();
    return --chunks;
  }
}
