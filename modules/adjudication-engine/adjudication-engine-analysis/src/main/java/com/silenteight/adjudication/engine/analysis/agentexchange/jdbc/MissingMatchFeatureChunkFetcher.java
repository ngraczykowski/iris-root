package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.agentexchange.MissingMatchFeatureReader.ChunkHandler;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeature;
import com.silenteight.adjudication.engine.analysis.agentexchange.domain.MissingMatchFeatureChunk;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class MissingMatchFeatureChunkFetcher implements AutoCloseable {

  private final ResultSet resultSet;
  private final int chunkSize;
  private final RowMapper<MissingMatchFeature> rowMapper;
  private final ChunkHandler chunkHandler;

  private final List<MissingMatchFeature> currentChunk;

  MissingMatchFeatureChunkFetcher(
      ResultSet resultSet, int chunkSize, RowMapper<MissingMatchFeature> rowMapper,
      ChunkHandler chunkHandler) {

    this.resultSet = resultSet;
    this.chunkSize = chunkSize;
    this.rowMapper = rowMapper;
    this.chunkHandler = chunkHandler;

    currentChunk = new ArrayList<>(chunkSize);
  }

  int readInChunks() throws SQLException {
    int rowCount = 0;

    while (resultSet.next()) {
      if (appendRowToChunk()) {
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

  private boolean appendRowToChunk() throws SQLException {
    var row = rowMapper.mapRow(resultSet, resultSet.getRow());

    if (row == null || !row.isValid()) {
      log.warn("Skipping invalid row: row={}", row);
      return false;
    }

    currentChunk.add(row);
    return true;
  }

  private void flushChunk() {
    chunkHandler.handle(new MissingMatchFeatureChunk(currentChunk));
    currentChunk.clear();
  }

  private void finish() {
    try {
      chunkHandler.finished();
    } catch (Exception e) {
      log.warn("Ignoring exception on finishing chunk", e);
    }
  }

  @Override
  public void close() throws SQLException {
    resultSet.close();
  }
}
