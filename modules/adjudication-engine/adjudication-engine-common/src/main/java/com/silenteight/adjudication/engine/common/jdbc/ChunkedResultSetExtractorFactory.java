package com.silenteight.adjudication.engine.common.jdbc;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class ChunkedResultSetExtractorFactory<T> {

  @Getter
  private final int chunkSize;

  ChunkedResultSetExtractorFactory(int chunkSize) {
    Preconditions.checkArgument(chunkSize > 0, "Chunk size %s must be greater than 0", chunkSize);

    this.chunkSize = chunkSize;
  }

  ResultSetExtractor<Integer> create(
      @NonNull RowMapper<? extends T> rowMapper, @NonNull ChunkHandler<? super T> chunkHandler) {

    return new Extractor(rowMapper, chunkHandler);
  }

  private final class Extractor implements ResultSetExtractor<Integer> {

    private final RowMapper<? extends T> rowMapper;
    private final ChunkHandler<? super T> chunkHandler;
    private final List<T> currentChunk;

    private Extractor(RowMapper<? extends T> rowMapper, ChunkHandler<? super T> chunkHandler) {
      this.rowMapper = rowMapper;
      this.chunkHandler = chunkHandler;

      currentChunk = new ArrayList<>(chunkSize);
    }

    @Override
    public Integer extractData(ResultSet rs) throws SQLException {
      int rowCount = 0;

      while (rs.next()) {
        if (appendRowToChunk(rs)) {
          rowCount += 1;
        }

        if (currentChunk.size() < chunkSize) {
          continue;
        }

        flushChunk();
      }

      if (!currentChunk.isEmpty()) {
        flushChunk();
      }

      finish();

      return rowCount;
    }

    private boolean appendRowToChunk(ResultSet rs) throws SQLException {
      var row = rowMapper.mapRow(rs, rs.getRow());

      if (row == null) {
        log.warn("Skipping invalid row");
        return false;
      }

      currentChunk.add(row);
      return true;
    }

    private void flushChunk() {
      try {
        chunkHandler.handle(List.copyOf(currentChunk));
      } catch (Exception e) {
        log.error("Chunk handler failed to process a chunk: chunkSize={}, exception={}, message{}",
            currentChunk.size(), e.getClass().getName(), e.getMessage());
        throw e;
      }
      currentChunk.clear();
    }

    private void finish() {
      try {
        chunkHandler.finished();
      } catch (Exception e) {
        log.error("Chunk handler failed to finish: exception={}, message{}",
            e.getClass().getName(), e.getMessage());
        throw e;
      }
    }
  }
}
