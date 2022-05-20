package com.silenteight.adjudication.engine.analysis.matchsolution;

import static com.silenteight.adjudication.engine.analysis.matchsolution.MatchSolutionFixture.createUnsolvedMatchesChunk;

public class InMemoryUnsolvedMatchesReader implements UnsolvedMatchesReader {

  private int chunks = 10;

  @Override
  public int readInChunks(
      long analysisId,
      ChunkHandler chunkHandler) {
    chunkHandler.handle(createUnsolvedMatchesChunk(10));
    if (chunks == 1)
      chunkHandler.finished();
    return --chunks;
  }
}
