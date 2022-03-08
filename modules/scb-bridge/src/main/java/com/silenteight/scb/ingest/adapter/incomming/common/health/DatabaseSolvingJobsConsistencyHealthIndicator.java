package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelSolvingJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelSolvingJobProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DatabaseSolvingJobsConsistencyHealthIndicator
    extends DatabaseJobsConsistencyHealthIndicator implements HealthIndicator {

  private final ScbBridgeAlertOrderProperties alertOrderProperties;
  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingProperties;
  private final CbsConfigProperties cbsConfigProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingProperties;

  DatabaseSolvingJobsConsistencyHealthIndicator(
      CbsConfigProperties cbsConfigProperties,
      DataSource dataSource,
      ScbBridgeAlertOrderProperties alertOrderProperties,
      ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingProperties,
      ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingProperties) {
    super(dataSource);
    this.alertOrderProperties = alertOrderProperties;
    this.alertLevelSolvingProperties = alertLevelSolvingProperties;
    this.cbsConfigProperties = cbsConfigProperties;
    this.watchlistLevelSolvingProperties = watchlistLevelSolvingProperties;
  }

  @Override
  public Health health() {
    Map<String, String> result = new HashMap<>();

    checkAlertOrderPrerequisites(result);
    checkSolvingPrerequisites(result);

    if (result.containsValue(NOT_PRESENT) || result.containsValue(DB_ERROR)) {
      return Health.down().withDetails(result).build();
    } else {
      return Health.up().withDetails(result).build();
    }
  }

  private void checkAlertOrderPrerequisites(Map<String, String> result) {
    result.putAll(
        verifyIfTableExists("AlertOrder", alertOrderProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName = alertOrderProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists("AlertOrder", cbsHitsDetailsHelperViewName));
  }

  private void checkSolvingPrerequisites(Map<String, String> result) {
    if (shouldCheckAlertLevelSolvingProperties())
      checkAlertLevelSolvingPrerequisites(result, "AlertLevelSolving");

    if (shouldCheckWatchlistLevelSolvingProperties())
      checkWatchlistLevelSolvingPrerequisites(result, "WatchlistLevelSolving");
  }

  private void checkAlertLevelSolvingPrerequisites(Map<String, String> result, String prefix) {
    result.putAll(
        verifyIfTableExists(prefix, alertLevelSolvingProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName =
        alertLevelSolvingProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists(prefix, cbsHitsDetailsHelperViewName));

    result.putAll(verifyIfFunctionExists(cbsConfigProperties.getAckFunctionName()));
  }

  private void checkWatchlistLevelSolvingPrerequisites(Map<String, String> result, String prefix) {
    result.putAll(
        verifyIfTableExists(prefix, watchlistLevelSolvingProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName =
        watchlistLevelSolvingProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists(prefix, cbsHitsDetailsHelperViewName));

    result.putAll(verifyIfFunctionExists(cbsConfigProperties.getAckFunctionName()));
    result.putAll(
        verifyIfFunctionExists(cbsConfigProperties.getRecomFunctionName()));
  }

  private boolean shouldCheckAlertLevelSolvingProperties() {
    return alertLevelSolvingProperties.isEnabled();
  }

  private boolean shouldCheckWatchlistLevelSolvingProperties() {
    return watchlistLevelSolvingProperties.isEnabled();
  }

  private Map<String, String> verifyIfFunctionExists(String functionName) {
    String description = "Function name: " + functionName;
    try (
        Connection connection = dataSource.getConnection();
        final PreparedStatement preparedStatement =
            connection.prepareStatement(String.format(FUNCTION_QUERY, functionName))) {

      preparedStatement.execute();
    } catch (SQLException e) {
      return verifyAllowedOracleException(description, e);
    }

    return Map.of(description, PRESENT);
  }

  @Nonnull
  private static Map<String, String> verifyAllowedOracleException(
      String description, SQLException e) {
    if (e.getMessage().startsWith(WRONG_NUMBER_OR_TYPES_OF_ARGS_ERROR)) {
      return Map.of(description, PRESENT);
    }
    return Map.of(description, NOT_PRESENT);
  }
}
