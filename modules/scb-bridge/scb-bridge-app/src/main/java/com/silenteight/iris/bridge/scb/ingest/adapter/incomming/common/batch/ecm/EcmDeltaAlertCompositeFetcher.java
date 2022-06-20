/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.AlertComposite;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService.SPLIT_ID_CHARACTER;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
class EcmDeltaAlertCompositeFetcher {

  private final EcmRecordCompositeFetcher ecmRecordCompositeFetcher;
  private final GnsSyncDeltaService syncDeltaService;
  private final EcmRecordDecisionsFetcher ecmRecordDecisionsFetcher;
  private final DataSource ecmDataSource;
  private final DataSource externalDataSource;
  private final String deltaJobName;

  EcmDeltaAlertCompositeFetcher(
      EcmRecordDecisionsFetcher ecmRecordDecisionsFetcher, DataSource ecmDataSource,
      DataSource externalDataSource, EcmRecordCompositeFetcher ecmRecordCompositeFetcher,
      GnsSyncDeltaService syncDeltaService, String deltaJobName) {
    this.ecmRecordCompositeFetcher = ecmRecordCompositeFetcher;
    this.syncDeltaService = syncDeltaService;
    this.ecmRecordDecisionsFetcher = ecmRecordDecisionsFetcher;
    this.externalDataSource = externalDataSource;
    this.deltaJobName = deltaJobName;
    this.ecmDataSource = ecmDataSource;
  }

  public List<AlertComposite> fetch(List<ExternalId> externalIds) {
    try {
      return fetchInTransaction(externalIds);
    } catch (SQLException e) {
      log.error("Cannot fetch AlertComposite data!", e);
      return emptyList();
    }
  }

  private List<AlertComposite> fetchInTransaction(List<ExternalId> externalIds) throws
      SQLException {
    List<AlertComposite> result = new ArrayList<>();
    try (Connection connection = externalDataSource.getConnection()) {
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

      result.addAll(fetch(connection, externalIds));

      connection.commit();
    }
    return result;
  }

  protected List<AlertComposite> fetch(Connection connection, List<ExternalId> externalIds)
      throws SQLException {
    Map<ExternalId, List<Decision>> decisionsMap = fetchDecisions(externalIds);
    List<ExternalId> delta = determineDelta(externalIds, decisionsMap);

    if (delta.isEmpty())
      return emptyList();

    return ecmRecordCompositeFetcher.fetchRecordsWithDetails(connection, decisionsMap, delta);
  }

  private Map<ExternalId, List<Decision>> fetchDecisions(
      List<ExternalId> externalIds) throws SQLException {
    try (Connection connection = ecmDataSource.getConnection()) {
      return ecmRecordDecisionsFetcher.fetchDecisions(connection, externalIds);
    }
  }

  private List<ExternalId> determineDelta(
      List<ExternalId> externalIds, Map<ExternalId, List<Decision>> groupedDecisions) {
    Map<ExternalId, Integer> prevCounts = getPreviousRecordDecisionCounts(externalIds);

    return groupedDecisions
        .entrySet()
        .stream()
        .filter(getDeltaPredicate(prevCounts))
        .map(Entry::getKey)
        .collect(toList());
  }

  private Map<ExternalId, Integer> getPreviousRecordDecisionCounts(List<ExternalId> externalIds) {
    return syncDeltaService.findAllByExternalId(externalIds, deltaJobName);
  }

  @Nonnull
  private static Predicate<Entry<ExternalId, List<Decision>>> getDeltaPredicate(
      Map<ExternalId, Integer> prevCounts) {
    return d -> !prevCounts.containsKey(
        new ExternalId(
            createSystemIdWithWatchlistId(d),
            d.getKey().getWatchlistId())) || isDecisionListChanged(prevCounts, d);
  }

  private static boolean isDecisionListChanged(
      Map<ExternalId, Integer> prevCounts, Entry<ExternalId, List<Decision>> entry) {

    return prevCounts.get(
        new ExternalId(
            createSystemIdWithWatchlistId(entry),
            entry.getKey().getWatchlistId())) != entry.getValue().size();
  }

  @Nonnull
  private static String createSystemIdWithWatchlistId(Entry<ExternalId, List<Decision>> entry) {
    return entry.getKey().getSystemId() + SPLIT_ID_CHARACTER + entry.getKey().getWatchlistId();
  }
}
