/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
class RecordCompositeReader extends AbstractItemStreamItemReader<AlertComposite>
    implements InitializingBean {

  private final BetterJdbcCursorItemReader<String> externalSystemIdsReader;
  private final MultipleAlertCompositeFetcher multipleAlertCompositeFetcher;
  private final int oraclePageSize;

  private int currentSystemIdIndex;
  private boolean endOfIds;

  private final List<AlertComposite> preparedAlertComposites;

  @SuppressWarnings("squid:S1699")
  RecordCompositeReader(
      BetterJdbcCursorItemReader<String> externalSystemIdsReader,
      MultipleAlertCompositeFetcher multipleAlertCompositeFetcher,
      int oraclePageSize) {

    setName(RecordCompositeReader.class.getName());

    this.externalSystemIdsReader = externalSystemIdsReader;
    this.multipleAlertCompositeFetcher = multipleAlertCompositeFetcher;
    this.oraclePageSize = oraclePageSize;
    log.debug("oraclePageSize={}", oraclePageSize);

    preparedAlertComposites = new ArrayList<>(oraclePageSize);
  }

  @Nullable
  @Override
  public AlertComposite read() throws Exception {
    prepareRecordComposites();

    if (currentSystemIdIndex < preparedAlertComposites.size())
      return preparedAlertComposites.get(currentSystemIdIndex++);
    else
      return null;
  }

  private void prepareRecordComposites() throws Exception {
    if (currentSystemIdIndex < preparedAlertComposites.size())
      return;

    preparedAlertComposites.clear();

    List<String> systemIds = fetchSystemIds();
    while (!systemIds.isEmpty()) {
      List<AlertComposite> alertComposites = fetchRecordComposites(systemIds);
      log.info(
          "Fetched external system IDs: count={}, record composites: count={}", systemIds.size(),
          alertComposites.size());

      if (!alertComposites.isEmpty()) {
        preparedAlertComposites.addAll(alertComposites);
        currentSystemIdIndex = 0;
        return;
      }

      systemIds = fetchSystemIds();
    }
  }

  private List<AlertComposite> fetchRecordComposites(List<String> ids) {
    return multipleAlertCompositeFetcher.fetch(ids);
  }

  private List<String> fetchSystemIds() throws Exception {
    if (log.isTraceEnabled())
      log.trace("Fetching system ids.");

    if (endOfIds)
      return emptyList();

    List<String> ids = new ArrayList<>(oraclePageSize);

    for (int i = 0; i < oraclePageSize; i++) {
      String id = externalSystemIdsReader.read();

      if (id == null) {  // NOSONAR
        endOfIds = true;
        break;
      }

      ids.add(id);
    }

    if (log.isTraceEnabled())
      log.trace("Fetched system ids.");

    return ids;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    externalSystemIdsReader.afterPropertiesSet();
  }

  @Override
  public void open(@NotNull ExecutionContext executionContext) {
    try {
      externalSystemIdsReader.open(executionContext);
    } catch (ItemStreamException e) {
      Throwable cause = e.getCause();

      if (cause instanceof QueryTimeoutException) {
        throw new ItemStreamException(
            "Query to the Oracle database timed out! Consider increasing the query timeout"
                + " setting.", cause);
      } else {
        throw e;
      }
    }

    endOfIds = false;
  }

  @Override
  public void update(@NotNull ExecutionContext executionContext) {
    externalSystemIdsReader.update(executionContext);
  }

  @Override
  public void close() {
    externalSystemIdsReader.close();
  }
}
