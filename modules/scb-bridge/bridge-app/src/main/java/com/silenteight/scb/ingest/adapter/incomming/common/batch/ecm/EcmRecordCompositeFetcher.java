package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.QueryStatementHelper;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecordMapper;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.AlertComposite;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.SuspectDataFetcher;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.SuspectsCollection;
import com.silenteight.scb.ingest.adapter.incomming.common.metrics.AlertsFetchedEvent;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.context.ApplicationEventPublisher;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm.EcmQueryTemplates.RECORDS_QUERY;
import static java.time.Duration.between;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
class EcmRecordCompositeFetcher {

  private final EcmAlertCompositeRowProcessor ecmAlertCompositeRowProcessor;
  private final EcmFetcherConfiguration configuration;
  private final ApplicationEventPublisher eventPublisher;
  private final SuspectDataFetcher suspectDataFetcher;

  @SuppressWarnings("findsecbugs:SQL_INJECTION_JDBC")
  @Timed(
      value = "serp.scb.bridge.oracle.fetch.ecm.records-with-details.time",
      description = "Time taken to fetch ECM records with details")
  List<AlertComposite> fetchRecordsWithDetails(
      Connection connection,
      Map<ExternalId, List<Decision>> decisionsMap,
      List<ExternalId> delta) throws SQLException {

    if (delta.isEmpty())
      return emptyList();

    eventPublisher.publishEvent(new AlertsFetchedEvent(delta.size()));

    if (log.isDebugEnabled())
      log.debug("Fetching records with details");

    List<AlertComposite> result = new ArrayList<>();
    Instant start = Instant.now();
    var systemIds = getSystemIds(delta);

    try (PreparedStatement statement = prepareRecordsQueryStatement(connection, systemIds)) {
      statement.setFetchSize(delta.size());
      statement.setQueryTimeout(configuration.getTimeout());

      QueryStatementHelper.setQueryParameters(statement, systemIds);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          var alertRow = AlertRecordMapper.mapResultSet(resultSet);
          var suspects = getSuspects(connection, alertRow, delta);

          result.addAll(getAlertComposites(alertRow, decisionsMap, suspects));
        }
      }
    } catch (SQLTimeoutException e) {
      log.error(
          "Timeout on fetchingRecords after: {} sec", between(start, Instant.now()).toSeconds(), e);
      throw new SQLException(e);
    }

    if (log.isDebugEnabled())
      logFetchedSystemIds(result);

    return result;
  }

  private static List<String> getSystemIds(List<ExternalId> externalIds) {
    return externalIds.stream().map(ExternalId::getSystemId).collect(Collectors.toList());
  }

  private PreparedStatement prepareRecordsQueryStatement(Connection connection, List<String> ids)
      throws SQLException {
    String query =
        QueryStatementHelper.prepareQuery(RECORDS_QUERY, configuration.getDbRelationName(), ids);
    return connection.prepareStatement(query);
  }

  private static void logFetchedSystemIds(List<AlertComposite> result) {
    String fetchedSystemIds = result
        .stream()
        .map(AlertComposite::getSystemId)
        .collect(joining(", ", "[", "]"));

    log.debug("Fetched alerts: count={}, systemIds={}", result.size(), fetchedSystemIds);
  }

  private List<AlertComposite> getAlertComposites(
      AlertRecord alertRow,
      Map<ExternalId, List<Decision>> decisionsMap,
      SuspectsCollection suspects) {
    return ecmAlertCompositeRowProcessor.process(alertRow, decisionsMap, suspects);
  }

  private SuspectsCollection getSuspects(
      Connection connection, AlertRecord alertRecord, List<ExternalId> delta) {
    var systemId = alertRecord.getSystemId();
    var suspects = suspectDataFetcher.parseHitDetails(connection, alertRecord);
    var watchlistIds = delta.stream()
        .filter(r -> r.getSystemId().equals(systemId))
        .map(ExternalId::getWatchlistId)
        .collect(Collectors.toList());

    if (log.isDebugEnabled())
      log.debug("Delta hits: systemId={}, watchlistIds={}", systemId, watchlistIds);

    return new SuspectsCollection(
        suspects.streamAsSuspects()
            .filter(s -> watchlistIds.contains(s.getOfacId()))
            .collect(Collectors.toList()));
  }

  @Getter
  @AllArgsConstructor
  static class EcmFetcherConfiguration {

    @NonNull
    private final String dbRelationName;
    private final int timeout;
  }
}
