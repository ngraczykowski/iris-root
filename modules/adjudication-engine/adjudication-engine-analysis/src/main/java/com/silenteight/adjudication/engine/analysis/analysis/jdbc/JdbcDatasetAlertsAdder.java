package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.DatasetAlertsAdder;
import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAlertChunk;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

class JdbcDatasetAlertsAdder implements DatasetAlertsAdder {

  private final InsertAnalysisAlertsFromDatasetQuery query;

  JdbcDatasetAlertsAdder(DataSource dataSource, int chunkSize, int limit) {
    Preconditions.checkArgument(
        chunkSize <= limit, "Chunk size %s must be less than or equal to limit %s", chunkSize,
        limit);

    query = new InsertAnalysisAlertsFromDatasetQuery(dataSource, chunkSize, limit);
  }

  @Override
  public int addAlertsFromDataset(
      long fromDatasetId, long toAnalysisId, ChunkHandler chunkHandler) {

    int totalCount = 0;
    int addedCount;
    do {
      // NOTE(ahaczewski): All chunks have to be queued before delegating to the original
      //  `chunkHandler`, because the query executes in transaction, and the handler
      //  is also called in the transactional context. The handler is sending chunks as
      //  asynchronous messages, and that has to happen with the transaction already committed,
      //  otherwise message handlers might not see the committed state.
      var queuingChunkHandler = new QueuingChunkHandler(chunkHandler);

      addedCount = query.execute(fromDatasetId, toAnalysisId, queuingChunkHandler);
      totalCount += addedCount;

      queuingChunkHandler.flush();
    } while (addedCount > 0);

    return totalCount;
  }

  /**
   * Queues all chunks for flushing outside the transaction the query is executed in.
   */
  @RequiredArgsConstructor
  private static final class QueuingChunkHandler implements ChunkHandler {

    private final List<AnalysisAlertChunk> chunksQueue = new ArrayList<>();

    private final ChunkHandler delegate;

    @Override
    public void handle(AnalysisAlertChunk chunk) {
      chunksQueue.add(chunk);
    }

    private void flush() {
      chunksQueue.forEach(delegate::handle);
      chunksQueue.clear();
    }
  }
}
