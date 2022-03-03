package com.silenteight.customerbridge.common.batch;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.domain.GnsSyncDeltaService;
import com.silenteight.proto.serp.v1.alert.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j

class DeltaAlertCompositeFetcher extends BaseMultipleAlertCompositeFetcher {

  private final RecordCompositeFetcher recordCompositeFetcher;
  private final GnsSyncDeltaService syncDeltaService;
  private final String deltaJobName;

  @Builder
  DeltaAlertCompositeFetcher(
      RecordDecisionsFetcher decisionsFetcher, DataSource externalDataSource,
      RecordCompositeFetcher recordCompositeFetcher, GnsSyncDeltaService syncDeltaService,
      String deltaJobName) {
    super(decisionsFetcher, externalDataSource);
    this.recordCompositeFetcher = recordCompositeFetcher;
    this.syncDeltaService = syncDeltaService;
    this.deltaJobName = deltaJobName;
  }

  @Override
  protected List<AlertComposite> fetch(Connection connection, List<String> systemIds)
      throws SQLException {
    Map<String, List<Decision>> decisionsMap = fetchDecisions(connection, systemIds);
    List<String> delta = determineDelta(systemIds, decisionsMap);

    if (!delta.isEmpty())
      return recordCompositeFetcher.fetchRecordsWithDetails(connection, decisionsMap, delta);
    else
      return emptyList();
  }

  private Map<String, Integer> getPreviousRecordDecisionCounts(List<String> ids) {
    return syncDeltaService.findAllByAlertExternalId(ids, deltaJobName);
  }

  private List<String> determineDelta(
      List<String> systemIds, Map<String, List<Decision>> groupedDecisions) {
    Map<String, Integer> prevCounts = getPreviousRecordDecisionCounts(systemIds);

    return groupedDecisions
        .entrySet()
        .stream()
        .filter(getDeltaPredicate(prevCounts))
        .map(Entry::getKey)
        .collect(toList());
  }

  @Nonnull
  private static Predicate<Entry<String, List<Decision>>> getDeltaPredicate(
      Map<String, Integer> prevCounts) {
    return d -> !prevCounts.containsKey(d.getKey()) || isDecisionListChanged(prevCounts, d);
  }

  private static boolean isDecisionListChanged(
      Map<String, Integer> prevCounts, Entry<String, List<Decision>> entry) {
    return prevCounts.get(entry.getKey()) != entry.getValue().size();
  }
}
