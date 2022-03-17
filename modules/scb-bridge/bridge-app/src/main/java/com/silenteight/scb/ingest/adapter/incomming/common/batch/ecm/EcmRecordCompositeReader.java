package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.batch.AlertComposite;
import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
class EcmRecordCompositeReader extends BetterJdbcCursorItemReader<AlertComposite> {

  private final JdbcCursorItemReader<ExternalId> externalSystemIdsReader;
  private final EcmDeltaAlertCompositeFetcher ecmDeltaAlertCompositeFetcher;
  private final int oraclePageSize;

  private int currentSystemIdIndex;
  private boolean endOfIds;

  private final List<AlertComposite> preparedAlertComposites;

  @SuppressWarnings("squid:S1699")
  EcmRecordCompositeReader(
      JdbcCursorItemReader<ExternalId> externalSystemIdsReader,
      EcmDeltaAlertCompositeFetcher ecmDeltaAlertCompositeFetcher,
      int oraclePageSize) {

    setName(EcmRecordCompositeReader.class.getName());

    this.externalSystemIdsReader = externalSystemIdsReader;
    this.ecmDeltaAlertCompositeFetcher = ecmDeltaAlertCompositeFetcher;
    this.oraclePageSize = oraclePageSize;
    log.debug("oraclePageSize={}", oraclePageSize);

    preparedAlertComposites = new ArrayList<>(oraclePageSize);
  }

  @Override
  protected AlertComposite doRead() throws Exception {
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

    List<ExternalId> externalIds = fetchSystemIds();
    while (!externalIds.isEmpty()) {
      if (log.isDebugEnabled())
        log.debug("Fetched external system IDs: count={}", externalIds.size());

      List<AlertComposite> alertComposites = fetchRecordComposites(externalIds);

      if (!alertComposites.isEmpty()) {
        preparedAlertComposites.addAll(alertComposites);

        if (log.isDebugEnabled())
          log.debug("Fetched record composites: count={}", alertComposites.size());
        currentSystemIdIndex = 0;
        return;
      }

      externalIds = fetchSystemIds();
    }
  }

  private List<AlertComposite> fetchRecordComposites(List<ExternalId> externalIds) {
    return ecmDeltaAlertCompositeFetcher.fetch(externalIds);
  }

  private List<ExternalId> fetchSystemIds() throws Exception {
    if (log.isTraceEnabled())
      log.trace("Fetching system ids.");

    if (endOfIds)
      return emptyList();

    List<ExternalId> externalIds = new ArrayList<>(oraclePageSize);

    for (int i = 0; i < oraclePageSize; i++) {
      ExternalId id = externalSystemIdsReader.read();

      if (id == null) {  // NOSONAR
        endOfIds = true;
        break;
      }

      externalIds.add(id);
    }

    if (log.isTraceEnabled())
      log.trace("Fetched system ids.");

    return externalIds;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    externalSystemIdsReader.afterPropertiesSet();
  }

  @NotNull
  @Override
  public String getSql() {
    return externalSystemIdsReader.getSql();
  }

  @Override
  protected void cleanupOnClose() {
    // do nothing
    // externalSystemIdsReader manages the cursor on the close actions
  }

  @Override
  protected void openCursor(@NotNull Connection con) {
    // do nothing
    // externalSystemIdsReader manages the cursor on the open/update actions
  }

  @Override
  protected AlertComposite readCursor(@NotNull ResultSet rs, int currentRow) {
    // GnsRecordComposite isn't read from cursor
    return null;
  }

  @Override
  public void open(@NotNull ExecutionContext executionContext) {
    externalSystemIdsReader.open(executionContext);
    endOfIds = false;
  }

  @Override
  public void update(@NotNull ExecutionContext executionContext) {
    super.update(executionContext);
    externalSystemIdsReader.update(executionContext);
  }

  @Override
  public void close() {
    externalSystemIdsReader.close();
    super.close();
  }
}
