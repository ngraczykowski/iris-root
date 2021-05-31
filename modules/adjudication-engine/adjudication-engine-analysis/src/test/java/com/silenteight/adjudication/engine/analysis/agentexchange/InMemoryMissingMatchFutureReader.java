package com.silenteight.adjudication.engine.analysis.agentexchange;

import static com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFixtures.createMissingMatchFeatureChunk;

class InMemoryMissingMatchFutureReader implements MissingMatchFeatureReader {

  private int missingMatchFeatureCount = 100;

  @Override
  public int read(
      long analysisId,
      ChunkHandler chunkHandler) {
    chunkHandler.handle(createMissingMatchFeatureChunk(10));
    chunkHandler.finished();
    return missingMatchFeatureCount - 10 > 0 ? missingMatchFeatureCount -= 10 : 0;
  }
}
