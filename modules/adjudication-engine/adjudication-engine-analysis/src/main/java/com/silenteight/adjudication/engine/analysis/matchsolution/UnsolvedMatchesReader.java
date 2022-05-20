package com.silenteight.adjudication.engine.analysis.matchsolution;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.UnsolvedMatchesChunk;

/**
 * Takes into account matches that are:
 *
 * <ol>
 *   <li>ready, i.e., have all required category and feature values available, and</li>
 *   <li>do not have a solution yet.</li>
 * </ol>
 */
public interface UnsolvedMatchesReader {

  /**
   * @return number of unsolved matches read, 0 if there are no unsolved matches.
   */
  int readInChunks(long analysisId, ChunkHandler chunkHandler);

  interface ChunkHandler {

    void handle(UnsolvedMatchesChunk chunk);

    default void finished() {
    }
  }
}
