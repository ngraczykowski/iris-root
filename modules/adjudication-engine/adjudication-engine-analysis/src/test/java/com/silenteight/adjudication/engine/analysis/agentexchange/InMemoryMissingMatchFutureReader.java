package com.silenteight.adjudication.engine.analysis.agentexchange;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createMissingMatchFeatureChunk;

class InMemoryMissingMatchFutureReader implements MissingMatchFeatureReader {

  @Override
  public int read(long analysisId, ChunkHandler chunkHandler) {
    for (int i = 0; i < 10; ++i) {
      chunkHandler.handle(createMissingMatchFeatureChunk(10));
    }

    chunkHandler.finished();

    return 100;
  }
}
