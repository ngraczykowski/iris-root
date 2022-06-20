/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.health;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
class DatabaseLearningJobsConsistencyHealthIndicator
    extends DatabaseJobsConsistencyHealthIndicator implements HealthIndicator {

  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  public static final String ALERT_LEVEL_LEARNING = "Alert level learning";
  public static final String ECM_LEARNING = "ECM learning";

  DatabaseLearningJobsConsistencyHealthIndicator(
      DataSource dataSource,
      ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties,
      EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties) {
    super(dataSource);
    this.alertLevelLearningJobProperties = alertLevelLearningJobProperties;
    this.ecmBridgeLearningJobProperties = ecmBridgeLearningJobProperties;
  }

  @Override
  public Health health() {
    Map<String, String> result = new HashMap<>();

    checkAlertLevelLearningPrerequisites(result);
    checkEcmLearningPrerequisites(result);

    if (result.containsValue(NOT_PRESENT) || result.containsValue(DB_ERROR)) {
      log.error("DatabaseLearningJobsConsistencyHealthIndicator set health down");
      return Health.down().withDetails(result).build();
    } else {
      return Health.up().withDetails(result).build();
    }
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
