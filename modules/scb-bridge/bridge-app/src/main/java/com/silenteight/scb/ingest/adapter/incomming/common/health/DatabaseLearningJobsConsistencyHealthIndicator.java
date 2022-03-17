package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelLearningJobProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class DatabaseLearningJobsConsistencyHealthIndicator
    extends DatabaseJobsConsistencyHealthIndicator implements HealthIndicator {

  private final ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  public static final String ALERT_LEVEL_LEARNING = "Alert level learning";
  public static final String ECM_LEARNING = "ECM learning";
  public static final String WATCHLIST_LEVEL_LEARNING = "Watchlist level learning";

  DatabaseLearningJobsConsistencyHealthIndicator(
      DataSource dataSource,
      ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties,
      ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties,
      EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties) {
    super(dataSource);
    this.watchlistLevelLearningJobProperties = watchlistLevelLearningJobProperties;
    this.alertLevelLearningJobProperties = alertLevelLearningJobProperties;
    this.ecmBridgeLearningJobProperties = ecmBridgeLearningJobProperties;
  }

  @Override
  public Health health() {
    Map<String, String> result = new HashMap<>();

    checkWatchlistLevelLearningPrerequisites(result);
    checkAlertLevelLearningPrerequisites(result);
    checkEcmLearningPrerequisites(result);

    if (result.containsValue(NOT_PRESENT) || result.containsValue(DB_ERROR)) {
      return Health.down().withDetails(result).build();
    } else {
      return Health.up().withDetails(result).build();
    }
  }

  private void checkWatchlistLevelLearningPrerequisites(Map<String, String> result) {
    if (!watchlistLevelLearningJobProperties.isEnabled())
      return;

    result.putAll(
        verifyIfTableExists(
            WATCHLIST_LEVEL_LEARNING, watchlistLevelLearningJobProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName =
        watchlistLevelLearningJobProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists(WATCHLIST_LEVEL_LEARNING, cbsHitsDetailsHelperViewName));
  }

  private void checkAlertLevelLearningPrerequisites(Map<String, String> result) {
    if (!alertLevelLearningJobProperties.isEnabled())
      return;

    result.putAll(
        verifyIfTableExists(
            ALERT_LEVEL_LEARNING, alertLevelLearningJobProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName =
        alertLevelLearningJobProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists(ALERT_LEVEL_LEARNING, cbsHitsDetailsHelperViewName));
  }

  private void checkEcmLearningPrerequisites(Map<String, String> result) {
    if (!ecmBridgeLearningJobProperties.isEnabled())
      return;

    result.putAll(
        verifyIfTableExists(ECM_LEARNING, ecmBridgeLearningJobProperties.getDbRelationName()));

    String cbsHitsDetailsHelperViewName =
        ecmBridgeLearningJobProperties.getCbsHitsDetailsHelperViewName();
    if (isNotBlank(cbsHitsDetailsHelperViewName))
      result.putAll(verifyIfTableExists(ECM_LEARNING, cbsHitsDetailsHelperViewName));
  }
}
