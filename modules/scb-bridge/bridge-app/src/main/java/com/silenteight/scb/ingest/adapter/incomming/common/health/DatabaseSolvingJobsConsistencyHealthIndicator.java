package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DatabaseSolvingJobsConsistencyHealthIndicator
    extends DatabaseJobsConsistencyHealthIndicator implements HealthIndicator {

  private final ScbBridgeAlertOrderProperties alertOrderProperties;

  DatabaseSolvingJobsConsistencyHealthIndicator(
      DataSource dataSource,
      ScbBridgeAlertOrderProperties alertOrderProperties) {
    super(dataSource);
    this.alertOrderProperties = alertOrderProperties;
  }

  @Override
  public Health health() {
    Map<String, String> result = new HashMap<>();

    checkAlertOrderPrerequisites(result);

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
}
