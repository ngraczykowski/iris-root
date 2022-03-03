package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.AlertRecordMapper;
import com.silenteight.customerbridge.common.config.FetcherConfiguration;
import com.silenteight.customerbridge.common.metrics.AlertsFetchedEvent;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.context.ApplicationEventPublisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.silenteight.customerbridge.cbs.batch.QueryStatementHelper.prepareQuery;
import static com.silenteight.customerbridge.cbs.batch.QueryStatementHelper.setQueryParameters;
import static com.silenteight.customerbridge.common.batch.QueryTemplates.RECORDS_QUERY;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
class RecordCompositeFetcher {

  private final AlertCompositeRowProcessor alertCompositeRowProcessor;
  private final FetcherConfiguration configuration;
  private final SuspectDataFetcher suspectDataFetcher;
  private final ApplicationEventPublisher eventPublisher;

  @SuppressWarnings("findsecbugs:SQL_INJECTION_JDBC")
  Optional<AlertComposite> fetchRecordWithDetails(
      Connection connection, List<Decision> decisions, String id) throws SQLException {
    if (log.isDebugEnabled())
      log.debug("Fetching record with details: decisionsCount={}", decisions.size());

    List<AlertComposite> alertComposites = fetchRecordsWithDetails(
        connection, Map.of(id, decisions), singletonList(id));
    int alertCount = alertComposites.size();

    if (alertCount > 1) {
      log.warn("Expected to fetch 1 alert: got={}", alertCount);
      throw new MultipleAlertsFoundException(id, alertCount);
    }

    if (log.isDebugEnabled())
      log.debug("Fetched records: count={}", alertCount);

    if (alertCount == 1)
      return Optional.of(alertComposites.get(0));

    return Optional.empty();
  }

  @SuppressWarnings("findsecbugs:SQL_INJECTION_JDBC")
  @Timed(
      value = "serp.scb.bridge.oracle.fetch.records-with-details.time",
      description = "Time taken to fetch records with details")
  List<AlertComposite> fetchRecordsWithDetails(
      Connection connection,
      Map<String, List<Decision>> decisionsMap,
      List<String> ids) throws SQLException {

    eventPublisher.publishEvent(new AlertsFetchedEvent(ids.size()));

    if (log.isDebugEnabled())
      log.debug("Fetching records with details");

    List<AlertComposite> result = new ArrayList<>();

    try (PreparedStatement statement = prepareRecordsQueryStatement(connection, ids)) {
      statement.setFetchSize(ids.size());
      statement.setQueryTimeout(configuration.getTimeout());
      setQueryParameters(statement, ids);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          result.addAll(getAlertComposites(connection, resultSet, decisionsMap));
        }
      }
    } catch (SQLTimeoutException e) {
      log.error("Timeout on fetchingRecords after: {} seconds", configuration.getTimeout(), e);
      throw new SQLException(e);
    }

    if (log.isDebugEnabled())
      logFetchedSystemIds(result);

    return result;
  }

  private PreparedStatement prepareRecordsQueryStatement(Connection connection, List<String> ids)
      throws SQLException {
    String query = prepareQuery(RECORDS_QUERY, configuration.getDbRelationName(), ids);
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
      Connection connection,
      ResultSet resultSet,
      Map<String, List<Decision>> decisionsMap) throws SQLException {

    AlertRecord alertRecord = AlertRecordMapper.mapResultSet(resultSet);
    SuspectsCollection suspects = suspectDataFetcher.parseHitDetails(connection, alertRecord);
    DecisionsCollection decisions = new DecisionsCollection(
        decisionsMap.getOrDefault(alertRecord.getSystemId(), emptyList()));
    return alertCompositeRowProcessor.process(alertRecord, suspects, decisions);
  }

  private static class MultipleAlertsFoundException extends RuntimeException {

    private static final long serialVersionUID = 5917854630525119300L;

    MultipleAlertsFoundException(String id, int foundCount) {
      super("Expected to fetch one alert with ID " + id + ", instead got " + foundCount);
    }
  }

}
